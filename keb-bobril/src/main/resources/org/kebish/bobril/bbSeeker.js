"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};

/*
Sample search expressions:
'*'                                     = any tag,
'div'                                   = all divs,
'div.bobwai--panel'                     = all divs with bobril component ID == 'bobwai--panel',
'.bobwai--panel'                        = all components with bobril component ID == bobwai--panel
'div.bobwai--panel[2]'                  = 3rd div with bobril component ID == 'bobwai--panel',
'.bobwai--panel[2]'                     = 3rd component with bobril component ID == 'bobwai--panel',
'div.bobwai--panel[2]/input'            = all inputs which are children of the div from the previous example,
'div.id1/div.id2[5]/div'                = all divs which are children of divs.id2 on 5th index which have a div.id1 as a parent
'div.bobwai--panel/div[text=INSIGHTS]'  = all divs with text INSIGHTS whose parent is div with bobril ID == bobwai--panel
'.id1/~.id2[text=Messenger]'            = all id2 elements "somewhere" inside id1 element (also works for tags)
'input[@placeholder=New Password]'      = all inputs with attribute "placeholder" and attribute value "New Password" (should work for any attribute)

'div.bobwai--app-header-button/*[text=_APPLICATIONS]'
 = any tag with text _APPLICATIONS whose parent is div with bobril ID == bobwai--app-header-button
*/
var BBSeeker;
(function (BBSeeker) {
    //represents bobril, e.g. definition (bb) is not imported in compiled javascript, it is used only to provide data model for TypeScript during coding
    var replacementChar = "-";
    BBSeeker.frameCounter = 0;
    BBSeeker.lastClickX = 0;
    BBSeeker.lastClickY = 0;
    document.addEventListener("click", function (event) {
        BBSeeker.lastClickX = event.clientX;
        BBSeeker.lastClickY = event.clientY;
    });
    var originalAfterFrame = b.setAfterFrame(function (c) {
        originalAfterFrame(c);
        BBSeeker.frameCounter++;
    });
    /**
     * Performs recursive search of a page virtual DOM starting from bobril root objects. All matching objects are returned as instances of HTMLElement.
     * @param expression search expression, see examples above.
     * @param root (optional) specify element which will serve as search root
     */
    function findElements(expression, root) {
        var result = [];
        var work = findElementsBody(expression, root);
        for (var i = 0; i < work.length; i++) {
            if (work[i].element != undefined) {
                result.push(work[i].element);
            }
            else {
                throw new BBSeekerError("Virtual components present in the result set. Please revise and fix your search expression to target a non-virtual component.", ErrorType.SEARCH);
            }
        }
        return result;
    }
    BBSeeker.findElements = findElements;
    /**
     * Waits up to a given time for an element to be available and returns it or returns a timeout error. Search is scheduled every 100ms if no browser frame updates were not detected in this time frame.
     * @param expression search expression
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [element[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function findElementsWithTimeout(expression, timeout, callback, root) {
        BBSeeker.frameCounter = 0;
        var start = new Date().getTime();
        var end = start + Math.abs(timeout);
        findElementsWithTimeoutBody(BBSeeker.frameCounter, expression, timeout, start, end, callback, root);
    }
    BBSeeker.findElementsWithTimeout = findElementsWithTimeout;
    /**
     * Waits up to a given time for an element to be available and returns promise which returns result or timeout error. Search is scheduled every 100ms if no browser frame updates were not detected in this time frame.
     * Used by Bobwai tests
     * @param expression search expression
     * @param timeout time to wait
     * @param callback which returns search results
     * @param root (optional) specify element which will serve as search root
     */
    function findElementsWithTimeoutAsync(expression, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, findElementsWithTimeoutBody(BBSeeker.frameCounter, expression, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.findElementsWithTimeoutAsync = findElementsWithTimeoutAsync;
    /**
     * Waits up to a given time for element to be not present.
     * @param expression search expression
     * @param timeout time to wait
     * @param callback true if element is not available, false if timeout occured and element is still present
     * @param root (optional) specify element which will serve as search root
     */
    function waitForElementNotPresent(expression, timeout, callback, root) {
        BBSeeker.frameCounter = 0;
        var start = new Date().getTime();
        var end = start + Math.abs(timeout);
        waitForElementNotPresentInternal(expression, timeout, start, end, callback, root);
    }
    BBSeeker.waitForElementNotPresent = waitForElementNotPresent;
    /**
     * Waits up to a given time for element to be not present. Returns promise which returns results or timeout error.
     * Used by Bobwai tests.
     * @param expression search expression
     * @param timeout time to wait
     * @param callback true if element is not available, false if timeout occured and element is still present
     * @param root (optional) specify element which will serve as search root
     */
    function waitForElementNotPresentAsync(expression, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, waitForElementNotPresentInternal(expression, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.waitForElementNotPresentAsync = waitForElementNotPresentAsync;
    /**
     * Returns selected element attribute node value.
     * @param expression BBSeeker search expression
     * @param attributeName attribute node name
     * @param root (optional) specify element which will serve as search root
     */
    function getAttribute(expression, attributeName, root) {
        var result = [];
        var work = findElementsBody(expression, root);
        for (var i = 0; i < work.length; i++) {
            var elm = work[i].element;
            if (elm != undefined) {
                var attrValue = elm["attributes"][attributeName];
                if (attrValue == undefined) {
                    attrValue = elm[attributeName];
                }
                // JSON.stringify will return undefined if attrValue is undefined as well
                if (typeof attrValue !== "string") {
                    attrValue = JSON.stringify(attrValue);
                }
                result.push(attrValue);
            }
            else {
                throw new BBSeekerError("Virtual components present in the result set. Please revise and fix your search expression to target a non-virtual component.", ErrorType.SEARCH);
            }
        }
        return result;
    }
    BBSeeker.getAttribute = getAttribute;
    /**
     * Returns selected bobril data node value.
     * @param expression BBSeeker search expression
     * @param dataName data node name
     * @param root (optional) specify element which will serve as search root
     */
    function getData(expression, dataName, root) {
        var result = [];
        var work = findElementsBody(expression, root);
        for (var i = 0; i < work.length; i++) {
            var dataNode = work[i].data;
            var dataValue = void 0;
            if (dataNode != undefined) {
                dataValue = dataNode[dataName];
                if (typeof dataValue !== "string") {
                    dataValue = JSON.stringify(dataValue);
                }
            }
            result.push(dataValue);
        }
        return result;
    }
    BBSeeker.getData = getData;
    /**
     * Returns input elements linked to the matched file selector components.
     * @param expression BBSeeker search expression
     * @param root (optional) specify element which will serve as search root
     */
    function getFileInput(expression, root) {
        return getFileInputInternal(expression, "fileInput", root);
    }
    BBSeeker.getFileInput = getFileInput;
    function getFileInputInternal(expression, propertyName, root) {
        var result = [];
        var work = getCtxInternal(expression, propertyName, root);
        for (var i = 0; i < work.length; i++) {
            var element = work[i];
            if (element != undefined) {
                result.push(element);
            }
        }
        return result;
    }
    /**
     * Returns selected bobril context value.
     * @param expression BBSeeker search expression
     * @param contextPropertyName context property name
     * @param root (optional) specify element which will serve as search root
     */
    function getCtx(expression, contextPropertyName, root) {
        var result = [];
        var work = getCtxInternal(expression, contextPropertyName, root);
        for (var i = 0; i < work.length; i++) {
            result.push(stringifyNonStringValue(work[i]));
        }
        return result;
    }
    BBSeeker.getCtx = getCtx;
    /**
     * Returns selected bobril context value.
     * @param expression BBSeeker search expression
     * @param contextPropertyName context property name
     * @param root (optional) specify element which will serve as search root
     */
    function getCtxInternal(expression, contextPropertyName, root) {
        var result = [];
        var work = findElementsBody(expression, root);
        for (var i = 0; i < work.length; i++) {
            var context = work[i].ctx;
            var contextValue = void 0;
            if (context != undefined) {
                contextValue = context[contextPropertyName];
            }
            result.push(contextValue);
        }
        return result;
    }
    function stringifyNonStringValue(value) {
        var strValue;
        if (typeof value !== "string") {
            strValue = JSON.stringify(value);
        }
        else {
            strValue = value;
        }
        return strValue;
    }
    /**
     * Finds all matching elements and extracts selected attribute value into a resultset.
     * @param expression BBSeeker search expression
     * @param attributeName attribute node name
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [string[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function getAttributeWithTimeout(expression, attributeName, timeout, callback, root) {
        BBSeeker.frameCounter = 0;
        var start = new Date().getTime();
        var end = start + Math.abs(timeout);
        getAttributeWithTimeoutBody(BBSeeker.frameCounter, expression, attributeName, timeout, start, end, callback, root);
    }
    BBSeeker.getAttributeWithTimeout = getAttributeWithTimeout;
    /**
     * Finds all matching elements and extracts selected attribute value into a resultset which is returned as promise result.
     * Used by Bobwai tests.
     * @param expression BBSeeker search expression
     * @param attributeName attribute node name
     * @param timeout time to wait
     * @param callback returns search results.
     * @param root (optional) specify element which will serve as search root
     */
    function getAttributeWithTimeoutAsync(expression, attributeName, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, getAttributeWithTimeoutBody(BBSeeker.frameCounter, expression, attributeName, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.getAttributeWithTimeoutAsync = getAttributeWithTimeoutAsync;
    /**
     * Finds all matching elements and extracts selected data value into a resultset.
     * @param expression BBSeeker search expression
     * @param dataName data node name
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [string[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function getDataWithTimeout(expression, dataName, timeout, callback, root) {
        BBSeeker.frameCounter = 0;
        var start = new Date().getTime();
        var end = start + Math.abs(timeout);
        getDataWithTimeoutBody(BBSeeker.frameCounter, expression, dataName, timeout, start, end, callback, root);
    }
    BBSeeker.getDataWithTimeout = getDataWithTimeout;
    /**
     * Returns promise which returns all matching elements and extracts selected data value into a resultset.
     * Used by Bobwai tests.
     * @param expression BBSeeker search expression
     * @param dataName data node name
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [string[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function getDataWithTimeoutAsync(expression, dataName, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, getDataWithTimeoutBody(BBSeeker.frameCounter, expression, dataName, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.getDataWithTimeoutAsync = getDataWithTimeoutAsync;
    /**
     * Finds matching file selection component and extracts reference to HTML element from ctx into a resultset which is returned as promise result.
     * @param expression BBSeeker search expression
     * @param timeout time to wait
     * @param callback returns search results.
     * @param root (optional) specify element which will serve as search root
     */
    function getFileInputWithTimeout(expression, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, getFileInputWithTimeoutBody(BBSeeker.frameCounter, expression, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.getFileInputWithTimeout = getFileInputWithTimeout;
    /**
     * Returns promise which contains reference to HTML input element referenced by file selection component.
     * Used by Bobwai tests.
     * @param expression BBSeeker search expression
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [HTMLElement[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function getFileInputWithTimeoutAsync(expression, dataName, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, getFileInputWithTimeoutBody(BBSeeker.frameCounter, expression, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.getFileInputWithTimeoutAsync = getFileInputWithTimeoutAsync;
    /**
     * Finds all matching elements and extracts selected context property value into a resultset.
     * @param expression BBSeeker search expression
     * @param ctxPropertyName context property name
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [string[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function getCtxWithTimeout(expression, ctxPropertyName, timeout, callback, root) {
        BBSeeker.frameCounter = 0;
        var start = new Date().getTime();
        var end = start + Math.abs(timeout);
        getCtxWithTimeoutBody(BBSeeker.frameCounter, expression, ctxPropertyName, timeout, start, end, callback, root);
    }
    BBSeeker.getCtxWithTimeout = getCtxWithTimeout;
    /**
     * Returns promise which returns all matching elements and extracts selected context value into a resultset.
     * Used by Bobwai tests.
     * @param expression BBSeeker search expression
     * @param ctxPropertyName context property name
     * @param timeout time to wait
     * @param callback webdriver callback returned by an async function it is a tuple: [string[], errorString]
     * @param root (optional) specify element which will serve as search root
     */
    function getCtxWithTimeoutAsync(expression, ctxPropertyName, timeout, callback, root) {
        return __awaiter(this, void 0, void 0, function () {
            var start, end;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        BBSeeker.frameCounter = 0;
                        start = new Date().getTime();
                        end = start + Math.abs(timeout);
                        return [4 /*yield*/, getCtxWithTimeoutBody(BBSeeker.frameCounter, expression, ctxPropertyName, timeout, start, end, callback, root)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    BBSeeker.getCtxWithTimeoutAsync = getCtxWithTimeoutAsync;
    /**
     * Returns last click coordinates.
     */
    function getLastClickPosition() {
        return [BBSeeker.lastClickX, BBSeeker.lastClickY];
    }
    BBSeeker.getLastClickPosition = getLastClickPosition;
    /**
     * Returns raw results from which searched types of objects can be extracted.
     * @param expression bbseeker search expression
     * @param root element which will serve as search root
     */
    function findElementsBody(expression, root) {
        var parsedExpression = parseExpression(expression);
        var work = [];
        if (root == undefined) {
            if (b == undefined) {
                throw new BBSeekerError("Bobril not found in the page. Search terminated.", ErrorType.BOBRIL);
            }
            var roots = b.getRoots();
            var keys = Object.keys(roots);
            for (var ri = 0; ri < keys.length; ri++) {
                var rtc = roots[keys[ri]].c;
                if (rtc != undefined) {
                    work = work.concat(rtc);
                }
                else {
                    //fallback of the last hope - if this fails, something is very wrong with Bobril!!!
                    rtc = roots[keys[ri]].n;
                    if (rtc != undefined && rtc.children != undefined) {
                        work = work.concat(rtc);
                    }
                }
            }
        }
        else {
            work.push(b.deref(root));
        }
        for (var i = 0; i < parsedExpression.length; i++) {
            var temp = [];
            var locator = parsedExpression[i];
            for (var j = 0; j < work.length; j++) {
                if (locator.siblingOffset == 0) {
                    if (locator.childIndexFilter != undefined) {
                        var matchingChildren = [];
                        matchingChildren = findElementsInternal(matchingChildren, work[j], locator, i === 0);
                        //child index: returned collection should be filled only to the level of the index, we return only one match per parent
                        //see matchObjectByFilter for more comments
                        if (locator.childIndexFilter.comparison == Comparison.SIMPLE) { //child index filter -> index only
                            if (matchingChildren.length - 1 >= locator.childIndexFilter.matchedValue) {
                                temp.push(matchingChildren[locator.childIndexFilter.matchedValue]);
                            }
                        }
                        else if (matchingChildren.length > 0) { // child index filter -> last(), possibly with last index offset (e.g. last()-1)
                            var lastIndexWithOffset = matchingChildren.length - 1 + locator.childIndexFilter.matchedValue;
                            if (lastIndexWithOffset >= 0) {
                                temp.push(matchingChildren[lastIndexWithOffset]);
                            }
                        }
                    }
                    else {
                        temp = findElementsInternal(temp, work[j], locator, i === 0);
                    }
                }
                else {
                    var parent_1 = work[j].parent;
                    if (parent_1 && parent_1.children) {
                        for (var k = 0; k < parent_1.children.length; k++) {
                            var child = parent_1.children[k];
                            var childWithOffset = parent_1.children[k + locator.siblingOffset];
                            if (child === work[j] && childWithOffset != undefined) {
                                temp.push(childWithOffset);
                                break;
                            }
                        }
                    }
                }
            }
            if (Array.isArray(locator.filters) && locator.filters.length == 1 && locator.filters[0].isIndexFilter()) {
                var index = parseInt(locator.filters[0].matchedValue);
                if (locator.filters[0].matchedName == undefined) {
                    temp = temp.slice(index, index + 1);
                }
                else {
                    if (temp.length + index < 0) {
                        var idx = (index != -1) ? index + 1 : "";
                        throw new BBSeekerError("Index filter: 'last()" + idx + "' is outside of result set length: '" + temp.length + "'", ErrorType.SEARCH);
                    }
                    temp = temp.slice(temp.length + index, temp.length + index + 1);
                }
            }
            work = temp;
        }
        return work;
    }
    /**
     * Recursive search wrapper.
     * @param resultArray array to push results into
     * @param bobrilObject bobril object to search for matching children
     * @param locator identifier used to match search to
     * @param anylevel true only for the 1st iteration
     */
    function findElementsInternal(resultArray, bobrilObject, locator, anylevel) {
        if (anylevel) {
            matchObject(bobrilObject, resultArray, locator);
        }
        if (locator.tag != "^") {
            findElementsRecursive(bobrilObject, resultArray, locator, anylevel);
        }
        else if (bobrilObject.parent) {
            resultArray.push(bobrilObject.parent);
        }
        return resultArray;
    }
    /**
     * Performes a recursive search of a bobril object children based on a provided identifier object. All matches are pushed into a result array.
     * @param bobrilObject
     * @param resultArray
     * @param locator
     * @param anylevel indicates the 1st iteration of a recursion, false otherwise
     */
    function findElementsRecursive(bobrilObject, resultArray, locator, anylevel) {
        if (bobrilObject != undefined && Array.isArray(bobrilObject.children)) {
            for (var i = 0; i < bobrilObject.children.length; i++) {
                var currentLocator = locator;
                var child = bobrilObject.children[i];
                if (anylevel || child.tag == undefined) {
                    findElementsRecursive(child, resultArray, locator, anylevel);
                }
                else if (locator.tag != undefined && locator.tag.charAt(0) == "~" && locator.tag.length > 1) {
                    currentLocator = clone(locator);
                    currentLocator.tag = locator.tag.replace("~", "");
                    findElementsRecursive(child, resultArray, currentLocator, true);
                }
                else if (locator.id != undefined && locator.id.charAt(0) == "~" && locator.id.length > 1) {
                    currentLocator = clone(locator);
                    currentLocator.id = locator.id.replace("~", "");
                    findElementsRecursive(child, resultArray, currentLocator, true);
                }
                else if (locator.key != undefined && locator.key.charAt(0) == "~" && locator.key.length > 1) {
                    currentLocator = clone(locator);
                    currentLocator.key = locator.key.replace("~", "");
                    findElementsRecursive(child, resultArray, currentLocator, true);
                }
                matchObject(child, resultArray, currentLocator);
            }
        }
    }
    /**
     * Async search step result handling. Depending on search result and state either calls callback or handles rescheduling.
     * @param lastCheck
     * @param expression
     * @param timeout
     * @param start
     * @param end
     * @param callback
     * @param root element which will serve as search root
     */
    function findElementsWithTimeoutBody(lastCheck, expression, timeout, start, end, callback, root) {
        try {
            var time = new Date().getTime();
            if (lastCheck == BBSeeker.frameCounter) {
                var results = handleBobrilNotReadyForElements(findElements, expression, end, time, root);
                if (results.length > 0) {
                    callback([results, null]);
                }
                else {
                    if (time < end) {
                        findElementsWithTimeoutReschedule(expression, timeout, start, end, callback, root);
                    }
                    else {
                        callback([null, "Async search timed out after '" + ((new Date().getTime() - start) / 1000) + "s' for expression: '" + expression + "'"]);
                    }
                }
            }
            else {
                findElementsWithTimeoutReschedule(expression, timeout, start, end, callback, root);
            }
        }
        catch (err) {
            callback([null, formatError(err)]);
        }
    }
    /**
     * Used for rescheduling async search.
     * @param expression
     * @param timeout
     * @param start
     * @param end
     * @param callback webdriver callback returned by an async function it is a tuple: [element[], errorString]
     * @param root element which will serve as search root
     */
    function findElementsWithTimeoutReschedule(expression, timeout, start, end, callback, root) {
        var lastCheck = BBSeeker.frameCounter;
        setTimeout(function () {
            findElementsWithTimeoutBody(lastCheck, expression, timeout, start, end, callback, root);
        }, 100);
    }
    /**
     * Internal. Waits up to a given time for element to be not present.
     * @param expression search expression
     * @param timeout time to wait
     * @param callback true if element is not available, false if timeout occured and element is still present
     * @param root element which will serve as search root
     */
    function waitForElementNotPresentInternal(expression, timeout, start, end, callback, root) {
        var lastCheck = BBSeeker.frameCounter;
        setTimeout(function () {
            try {
                var time = new Date().getTime();
                if (lastCheck == BBSeeker.frameCounter) {
                    var results = findElements(expression, root);
                    if (results.length == 0) {
                        callback([true, null]);
                    }
                    else {
                        if (time < end) {
                            waitForElementNotPresentInternal(expression, timeout, start, end, callback, root);
                        }
                        else {
                            callback([false, null]);
                        }
                    }
                }
                else {
                    waitForElementNotPresentInternal(expression, timeout, start, end, callback, root);
                }
            }
            catch (err) {
                callback([false, formatError(err)]);
            }
        }, 100);
    }
    function getAttributeWithTimeoutBody(lastCheck, expression, attributeName, timeout, start, end, callback, root) {
        try {
            var time = new Date().getTime();
            if (lastCheck == BBSeeker.frameCounter) {
                var results = handleBobrilNotReadyForData(getAttribute, expression, attributeName, end, time, root);
                if (results.length > 0) {
                    callback([results, null]);
                }
                else {
                    if (time < end) {
                        getAttributeWithTimeoutReschedule(expression, attributeName, timeout, start, end, callback, root);
                    }
                    else {
                        callback([null, "Async search timed out after '" + ((new Date().getTime() - start) / 1000) + "s' for expression: '" + expression + "'"]);
                    }
                }
            }
            else {
                getAttributeWithTimeoutReschedule(expression, attributeName, timeout, start, end, callback, root);
            }
        }
        catch (err) {
            callback([null, formatError(err)]);
        }
    }
    function getAttributeWithTimeoutReschedule(expression, attributeName, timeout, start, end, callback, root) {
        var lastCheck = BBSeeker.frameCounter;
        setTimeout(function () {
            getAttributeWithTimeoutBody(lastCheck, expression, attributeName, timeout, start, end, callback, root);
        }, 100);
    }
    function getDataWithTimeoutBody(lastCheck, expression, dataName, timeout, start, end, callback, root) {
        try {
            var time = new Date().getTime();
            if (lastCheck == BBSeeker.frameCounter) {
                var results = handleBobrilNotReadyForData(getData, expression, dataName, end, time, root);
                if (results.length > 0) {
                    callback([results, null]);
                }
                else {
                    if (time < end) {
                        getDataWithTimeoutReschedule(expression, dataName, timeout, start, end, callback, root);
                    }
                    else {
                        callback([null, "Async search timed out after '" + ((new Date().getTime() - start) / 1000) + "s' for expression: '" + expression + "'"]);
                    }
                }
            }
            else {
                getDataWithTimeoutReschedule(expression, dataName, timeout, start, end, callback, root);
            }
        }
        catch (err) {
            callback([null, formatError(err)]);
        }
    }
    function getDataWithTimeoutReschedule(expression, dataName, timeout, start, end, callback, root) {
        var lastCheck = BBSeeker.frameCounter;
        setTimeout(function () {
            getDataWithTimeoutBody(lastCheck, expression, dataName, timeout, start, end, callback, root);
        }, 100);
    }
    function getCtxWithTimeoutBody(lastCheck, expression, ctxPropertyName, timeout, start, end, callback, root) {
        try {
            var time = new Date().getTime();
            if (lastCheck == BBSeeker.frameCounter) {
                var results = handleBobrilNotReadyForData(getCtx, expression, ctxPropertyName, end, time, root);
                if (results.length > 0) {
                    callback([results, null]);
                }
                else {
                    if (time < end) {
                        getCtxWithTimeoutReschedule(expression, ctxPropertyName, timeout, start, end, callback, root);
                    }
                    else {
                        callback([null, "Async search timed out after '" + ((new Date().getTime() - start) / 1000) + "s' for expression: '" + expression + "'"]);
                    }
                }
            }
            else {
                getCtxWithTimeoutReschedule(expression, ctxPropertyName, timeout, start, end, callback, root);
            }
        }
        catch (err) {
            callback([null, formatError(err)]);
        }
    }
    function getCtxWithTimeoutReschedule(expression, attributeName, timeout, start, end, callback, root) {
        var lastCheck = BBSeeker.frameCounter;
        setTimeout(function () {
            getCtxWithTimeoutBody(lastCheck, expression, attributeName, timeout, start, end, callback, root);
        }, 100);
    }
    function getFileInputWithTimeoutBody(lastCheck, expression, timeout, start, end, callback, root) {
        try {
            var time = new Date().getTime();
            if (lastCheck == BBSeeker.frameCounter) {
                var results = handleBobrilNotReadyForData(getFileInputInternal, expression, "fileInput", end, time, root);
                if (results.length > 0) {
                    callback([results, null]);
                }
                else {
                    if (time < end) {
                        getFileInputWithTimeoutReschedule(expression, timeout, start, end, callback, root);
                    }
                    else {
                        callback([null, "Async search timed out after '" + ((new Date().getTime() - start) / 1000) + "s' for expression: '" + expression + "'"]);
                    }
                }
            }
            else {
                getFileInputWithTimeoutReschedule(expression, timeout, start, end, callback, root);
            }
        }
        catch (err) {
            callback([null, formatError(err)]);
        }
    }
    function getFileInputWithTimeoutReschedule(expression, timeout, start, end, callback, root) {
        var lastCheck = BBSeeker.frameCounter;
        setTimeout(function () {
            getFileInputWithTimeoutBody(lastCheck, expression, timeout, start, end, callback, root);
        }, 100);
    }
    /**
     * Matches bobril object with provided identifier.
     * @param bobrilObject
     * @param resultArray
     * @param locator
     */
    function matchObject(bobrilObject, resultArray, locator) {
        if (locator.tag == undefined || bobrilObject.tag === locator.tag || locator.tag === "*") {
            if (locator.id != undefined && bobrilObject.component != undefined) {
                var matchedId = bobrilObject.component.id;
                if (matchedId != undefined && matchedId.indexOf("/") != -1) {
                    matchedId = matchedId.replace(/\//g, replacementChar);
                }
                if (matchedId === locator.id) {
                    matchObjectByFilters(bobrilObject, resultArray, locator);
                }
            }
            else if (locator.key != undefined) {
                if (locator.keyRegex == undefined) {
                    if (bobrilObject.key === locator.key) {
                        matchObjectByFilters(bobrilObject, resultArray, locator);
                    }
                }
                else if (bobrilObject.key != undefined) {
                    var matches = bobrilObject.key.match(locator.keyRegex);
                    if (matches != undefined && matches.length > 0 && matches[0] === bobrilObject.key) {
                        matchObjectByFilters(bobrilObject, resultArray, locator);
                    }
                }
            }
            else if (locator.id == undefined && locator.key == undefined) {
                matchObjectByFilters(bobrilObject, resultArray, locator);
            }
        }
    }
    /**
     * Matches bobril object with provided filters. Helper booleans are resolved ahead of time and provided as a parameter to improve performance.
     * @param bobrilObject
     * @param resultArray
     * @param locator
     */
    function matchObjectByFilters(bobrilObject, resultArray, locator) {
        if (locator.filters && (locator.filters.length == 0 || locator.filters[0].isIndexFilter())) {
            resultArray.push(bobrilObject);
        }
        else if (locator.filters) {
            var match = bobrilObject;
            for (var i = 0; i < locator.filters.length; i++) {
                var filter = locator.filters[i];
                if (match != undefined && filter.joinType == JoinType.AND) {
                    match = matchObjectByFilter(match, locator, filter);
                }
            }
            if (match != undefined) {
                resultArray.push(match);
            }
        }
    }
    function matchObjectByFilter(bobrilObject, locator, filter) {
        if (filter.isTextFilter()) {
            if (filter.isStrictFilter) {
                return matchStrictTextFilter(bobrilObject, locator, filter);
            }
            else {
                return matchNonStrictTextFilter(bobrilObject, locator, filter);
            }
        }
        else if (filter.isAttributeFilter() && bobrilObject.element != undefined) {
            if (filter.isStrictFilter) {
                return matchStrictAttributeFilter(bobrilObject, filter);
            }
            else {
                return matchNonStrictAttributeFilter(bobrilObject, filter);
            }
        }
        else if (filter.isDataFilter() && bobrilObject.data != undefined && filter && filter.matchedName && bobrilObject.data[filter.matchedName] != undefined) {
            if (filter.isStrictFilter) {
                return matchStrictValue(bobrilObject, bobrilObject.data[filter.matchedName], filter);
            }
            else {
                return matchNonStrictValue(bobrilObject, bobrilObject.data[filter.matchedName], filter);
            }
        }
        else if (filter.isChildIndexFilter()) { // results will be trimmed in the calling function or bz following filters on the same level
            return bobrilObject;
        }
        return;
    }
    function matchStrictValue(bobrilObject, testedValue, filter) {
        if (testedValue == undefined) {
            return;
        }
        if (typeof testedValue !== "string") {
            testedValue = JSON.stringify(testedValue);
        }
        if (Comparison.SIMPLE == filter.comparison) {
            if (testedValue == filter.matchedValue) {
                return bobrilObject;
            }
        }
        else if (Comparison.STARTS_WITH == filter.comparison) {
            if (startsWith(testedValue, filter.matchedValue)) {
                return bobrilObject;
            }
        }
        else if (Comparison.ENDS_WITH == filter.comparison) {
            if (endsWith(testedValue, filter.matchedValue)) {
                return bobrilObject;
            }
        }
        return;
    }
    function matchNonStrictValue(bobrilObject, testedValue, filter) {
        if (testedValue == undefined) {
            return;
        }
        if (typeof testedValue !== "string") {
            testedValue = JSON.stringify(testedValue);
        }
        if (testedValue.indexOf(filter.matchedValue) != -1) {
            return bobrilObject;
        }
        return;
    }
    function matchStrictTextFilter(bobrilObject, locator, filter) {
        if (Array.isArray(bobrilObject.children)) {
            for (var j = 0; j < bobrilObject.children.length; j++) {
                var textChild = bobrilObject.children[j];
                if (typeof textChild === "string") {
                    var match = matchStrictValue(textChild, textChild["children"], filter);
                    if (match != undefined) {
                        return sanitizeTextNode(match);
                    }
                }
                else {
                    if (textChild.tag == null) {
                        return matchObjectByFilter(textChild, locator, filter);
                    }
                }
            }
        }
        else {
            var match = matchStrictValue(bobrilObject, bobrilObject.children, filter);
            if (match != undefined) {
                return sanitizeTextNode(match);
            }
        }
        return undefined;
    }
    function matchNonStrictTextFilter(bobrilObject, locator, filter) {
        if (Array.isArray(bobrilObject.children)) {
            for (var j = 0; j < bobrilObject.children.length; j++) {
                var textChild = bobrilObject.children[j];
                if (typeof textChild === "string") {
                    var childText = textChild["children"];
                    if (childText != undefined && childText.indexOf(filter.matchedValue) != -1) {
                        return sanitizeTextNode(textChild);
                    }
                }
                else {
                    if (textChild && textChild.tag == null) {
                        return matchObjectByFilter(textChild, locator, filter);
                    }
                }
            }
        }
        else {
            var objectText = bobrilObject.children;
            if (objectText != undefined && objectText.indexOf(filter.matchedValue) != -1) {
                return sanitizeTextNode(bobrilObject);
            }
        }
        return;
    }
    function matchStrictAttributeFilter(bobrilObject, filter) {
        if (filter && filter.matchedName && bobrilObject.element["attributes"][filter.matchedName] != undefined) {
            return matchStrictValue(bobrilObject, bobrilObject.element["attributes"][filter.matchedName]["nodeValue"], filter);
        }
        else if (filter && filter.matchedName) {
            return matchStrictValue(bobrilObject, bobrilObject.element[filter.matchedName], filter);
        }
        else
            return undefined;
    }
    function matchNonStrictAttributeFilter(bobrilObject, filter) {
        if (filter && filter.matchedName && bobrilObject.element["attributes"][filter.matchedName] != undefined) {
            return matchNonStrictValue(bobrilObject, bobrilObject.element["attributes"][filter.matchedName]["nodeValue"], filter);
        }
        else if (filter && filter.matchedName) {
            return matchNonStrictValue(bobrilObject, bobrilObject.element[filter.matchedName], filter);
        }
        else
            return undefined;
    }
    function startsWith(text, start) {
        return text.slice(0, start.length) == start;
    }
    function endsWith(text, end) {
        return text.slice(-end.length) == end;
    }
    /**
     * Makes sure that resulting object is not a text node but an element that contains a text (e.g. <div>test</d> instead of "test").
     * @param bobrilObject
     */
    function sanitizeTextNode(bobrilObject) {
        if (bobrilObject.element != undefined && bobrilObject.element.nodeType == Node.TEXT_NODE) {
            return bobrilObject.parent;
        }
        else {
            return bobrilObject;
        }
    }
    /**
     * Handle case when bobril has not finished loading nodes yet - variant for elements.
     * @param search
     * @param expression
     * @param end
     * @param time
     * @param root element which will serve as search root
     */
    function handleBobrilNotReadyForElements(search, expression, end, time, root) {
        try {
            return search(expression, root);
        }
        catch (err) {
            if (err instanceof BBSeekerError && err.type == ErrorType.BOBRIL && (time < end)) {
                return [];
            }
            throw err;
        }
    }
    /**
     * Handle case when bobril has not finished loading nodes yet - variant for attributes and data.
     * @param search
     * @param expression
     * @param name
     * @param end
     * @param time
     * @param root element which will serve as search root
     */
    // ten search muze opravdu vracet cokoli ? asi to udelat zase genericky
    function handleBobrilNotReadyForData(search, expression, name, end, time, root) {
        try {
            return search(expression, name, root);
        }
        catch (err) {
            if (err instanceof BBSeekerError && err.type == ErrorType.BOBRIL && (time < end)) {
                return [];
            }
            throw err;
        }
    }
    function formatError(err) {
        if (err instanceof BBSeekerError) {
            return err.message;
        }
        return err.name + ": " + err.message;
    }
    /**
     * Parses provided search expression into an array of node identifiers.
     * @param expression search expression
     */
    function parseExpression(expression) {
        var identifiers = expression.split(/\/(?=(?:(?:[^\[\]]*\[[^\[\]]*\])|(?:[^\[\]]*\[[^\[\]]*\]))*[^\[\]]*$)/);
        var resultArray = [];
        for (var i = 0; i < identifiers.length; i++) {
            var componentId = identifiers[i];
            resultArray[i] = parseSelector(componentId);
        }
        return resultArray;
    }
    /**
     * Parses each selector substring into an Identifier object.
     * @param identifier
     */
    function parseSelector(identifier) {
        var tagAndId = extractTagAndId(identifier);
        var filters = extractFilters(identifier);
        var regexMatches = /^([~|^])?([^.|^#]*)?((.?)(.+)?)/g.exec(tagAndId);
        var id = undefined;
        var key = undefined;
        var keyRegex = undefined;
        var tag = undefined;
        var offset = 0;
        if (regexMatches !== null) {
            if ("^" == regexMatches[1]) {
                tag = regexMatches[1];
            }
            else {
                var anywhere = "~" == regexMatches[1];
                if (regexMatches[2]) {
                    tag = (anywhere) ? "~" + regexMatches[2] : regexMatches[2];
                }
                if (regexMatches[4]) {
                    switch (regexMatches[4]) {
                        case ".":
                            id = (anywhere) ? "~" + regexMatches[5] : regexMatches[5];
                            break;
                        case "#":
                            key = (anywhere) ? "~" + regexMatches[5] : regexMatches[5];
                            break;
                        default:
                            throw new BBSeekerError("Unmatched symbol '" + regexMatches[4] + "' in locator '" + identifier + "'", ErrorType.PARSER);
                    }
                }
            }
        }
        if (key != undefined && key.indexOf("*") != -1) {
            keyRegex = new RegExp(key.replace(/\*/g, ".*"), "g");
        }
        var rightSibling = tag ? /^>(\d*)>$/g.exec(tag) : null;
        var leftSibling = tag ? /^<(\d*)<$/g.exec(tag) : null;
        if (rightSibling) {
            offset = (!rightSibling[1]) ? 1 : Number(rightSibling[1]);
        }
        else if (leftSibling) {
            offset = (!leftSibling[1]) ? -1 : -1 * Number(leftSibling[1]);
        }
        return new Identifier(tag, id, key, keyRegex, filters, offset, getChildIndexFilter(filters));
    }
    function extractTagAndId(identifier) {
        if (identifier.indexOf('[') == -1) {
            return identifier;
        }
        return identifier.substring(0, identifier.indexOf('['));
    }
    function extractFilters(identifier) {
        if (identifier.indexOf('[') == -1) {
            return [];
        }
        var splits = identifier.substring(identifier.indexOf('[') + 1, identifier.length - 1).split(/(\]AND\[|\]OR\[)/i);
        if (splits.length > 1 && splits.length % 2 != 1) {
            throw new BBSeekerError("Unexpected number of splits while parsing filters: " + splits, ErrorType.PARSER);
        }
        var filters = [];
        for (var i = 0; i < splits.length; i = i + 2) {
            if (i == 0) {
                filters.push(parseFilter(splits[i], null));
            }
            else {
                var filter = parseFilter(splits[i], splits[i - 1]);
                if (filter.isIndexFilter()) {
                    throw new BBSeekerError("Index filter cannot be joined with other filters on the same level.", ErrorType.PARSER);
                }
                filters.push(filter);
            }
        }
        return filters;
    }
    function parseFilter(filterStr, joinSplitter) {
        var f = new Filter();
        var isFilterAttribute = false;
        var isFilterData = false;
        if (filterStr.charAt(0) == "@" && filterStr.length > 2) {
            if (filterStr.indexOf("*=") > 0 && filterStr.indexOf("*=") < filterStr.length - 2) {
                isFilterAttribute = true;
                f.comparison = Comparison.STARTS_WITH;
            }
            else if (filterStr.indexOf("^=") > 0 && filterStr.indexOf("^=") < filterStr.length - 2) {
                isFilterAttribute = true;
                f.comparison = Comparison.ENDS_WITH;
            }
            else {
                var strictness = parseStrictness(filterStr);
                if (strictness == Strictness.STRICT) {
                    isFilterAttribute = true;
                }
                else if (strictness == Strictness.NON_STRICT) {
                    isFilterAttribute = true;
                    f.isStrictFilter = false;
                }
            }
        }
        if (filterStr.charAt(0) == "$" && filterStr.length > 2) {
            if (filterStr.indexOf("*=") > 0 && filterStr.indexOf("*=") < filterStr.length - 2) {
                isFilterData = true;
                f.comparison = Comparison.STARTS_WITH;
            }
            else if (filterStr.indexOf("^=") > 0 && filterStr.indexOf("^=") < filterStr.length - 2) {
                isFilterData = true;
                f.comparison = Comparison.ENDS_WITH;
            }
            else {
                var strictness = parseStrictness(filterStr);
                if (strictness == Strictness.STRICT) {
                    isFilterData = true;
                }
                else if (strictness == Strictness.NON_STRICT) {
                    isFilterData = true;
                    f.isStrictFilter = false;
                }
            }
        }
        var childIndexRegexResult;
        if (!isNaN(Number(filterStr))) {
            f.matchedValue = filterStr;
        }
        else if (filterStr.indexOf("last()") == 0) {
            f.matchedName = "last";
            var indexDelta = -1;
            var index = indexDelta;
            if (/-(\s+)?\d+/.test(filterStr)) {
                index = parseInt(filterStr.replace(/ /g, "").replace("last()", "")) + indexDelta;
            }
            f.matchedValue = index;
        }
        else if (filterStr.indexOf("text=") == 0) {
            f.filterType = FilterType.TEXT;
            f.matchedValue = filterStr.replace("text=", "");
        }
        else if (filterStr.indexOf("text~") == 0) {
            f.filterType = FilterType.TEXT;
            f.matchedValue = filterStr.replace("text~", "");
            f.isStrictFilter = false;
        }
        else if (filterStr.indexOf("text*=") == 0) {
            f.filterType = FilterType.TEXT;
            f.matchedValue = filterStr.replace("text*=", "");
            f.comparison = Comparison.STARTS_WITH;
        }
        else if (filterStr.indexOf("text^=") == 0) {
            f.filterType = FilterType.TEXT;
            f.matchedValue = filterStr.replace("text^=", "");
            f.comparison = Comparison.ENDS_WITH;
        }
        else if (isFilterAttribute) {
            f.filterType = FilterType.ATTRIBUTE;
            var splitAttribute = extractFilterTuple(filterStr, f.isStrictFilter, f.comparison);
            f.matchedName = splitAttribute[0].replace("@", "");
            f.matchedValue = splitAttribute[1];
        }
        else if (isFilterData) {
            f.filterType = FilterType.DATA;
            var splitData = extractFilterTuple(filterStr, f.isStrictFilter, f.comparison);
            f.matchedName = splitData[0].replace("$", "");
            f.matchedValue = splitData[1];
        }
        else if (childIndexRegexResult = /^:([0-9]\d*|last\(\)(\-\d+)?)$/g.exec(filterStr.replace(/\s/g, ''))) {
            f.filterType = FilterType.CHILD_INDEX;
            //child index value or "last()"
            if (!isNaN(Number(childIndexRegexResult[1]))) {
                f.matchedValue = parseInt(childIndexRegexResult[1]); //specifix child index indexed from 0
                f.matchedName = f.matchedValue;
            }
            else {
                f.comparison = Comparison.COMPLEX; // complex is used for "last()"
                f.matchedName = childIndexRegexResult[1]; //e.g. last()-1
                f.matchedValue = childIndexRegexResult[2] != undefined ? parseInt(childIndexRegexResult[2]) : 0; //e.g. -1 from last()-1
            }
        }
        else {
            throw new BBSeekerError("Unrecognized filter: '" + filterStr + "'", ErrorType.PARSER);
        }
        if (joinSplitter != null) {
            switch (joinSplitter.toLowerCase()) {
                case "]and[":
                    f.joinType = JoinType.AND;
                    break;
                //case "]or[":
                //    f.joinType = JoinType.OR;
                //    break;
                default:
                    throw new BBSeekerError("Unmatched filter join option '..." + joinSplitter + "'...", ErrorType.PARSER);
            }
        }
        return f;
    }
    function parseStrictness(filterStr) {
        var equalsIndex = filterStr.indexOf("=");
        var tildeIndex = filterStr.indexOf("~");
        if (equalsIndex > 0 && tildeIndex > 0) {
            if (equalsIndex < tildeIndex) {
                return Strictness.STRICT;
            }
            return Strictness.NON_STRICT;
        }
        else if (equalsIndex > 0 && tildeIndex < 0 && equalsIndex < filterStr.length - 1) {
            return Strictness.STRICT;
        }
        else if (tildeIndex > 0 && equalsIndex < 0 && tildeIndex < filterStr.length - 1) {
            return Strictness.NON_STRICT;
        }
        return Strictness.NOT_VALID;
    }
    function extractFilterTuple(filter, strict, comparison) {
        switch (comparison) {
            case Comparison.SIMPLE:
            {
                var index = (strict) ? filter.indexOf("=") : filter.indexOf("~");
                return [filter.slice(0, index), filter.slice(index + 1, filter.length)];
            }
            case Comparison.STARTS_WITH:
            {
                var operator = "*=";
                var index = filter.indexOf(operator);
                return [filter.slice(0, index), filter.slice(index + operator.length, filter.length)];
            }
            case Comparison.ENDS_WITH:
            {
                var operator = "^=";
                var index = filter.indexOf(operator);
                return [filter.slice(0, index), filter.slice(index + operator.length, filter.length)];
            }
            default:
                throw new BBSeekerError("Unmatched comparison operator in filter '" + filter + "'.", ErrorType.PARSER);
        }
    }
    function getChildIndexFilter(filters) {
        var childIndexFilter = undefined;
        if (filters) {
            for (var _i = 0, filters_1 = filters; _i < filters_1.length; _i++) {
                var filter = filters_1[_i];
                if (filter.isChildIndexFilter()) {
                    if (childIndexFilter != undefined) {
                        throw new BBSeekerError("Only one child index filter is allowed per search level but multiple were detected "
                            + "'" + childIndexFilter.matchedName + ", " + filter.matchedValue + "'.", ErrorType.PARSER);
                    }
                    childIndexFilter = filter;
                }
            }
        }
        return childIndexFilter;
    }
    var Identifier = /** @class */ (function () {
        function Identifier(tag, id, key, keyRegex, filters, siblingOffset, childIndexFilter) {
            if (siblingOffset === void 0) { siblingOffset = 0; }
            if (childIndexFilter === void 0) { childIndexFilter = undefined; }
            this.tag = tag;
            this.id = id;
            this.key = key;
            this.keyRegex = keyRegex;
            this.filters = filters;
            this.siblingOffset = siblingOffset;
            this.childIndexFilter = childIndexFilter;
        }
        return Identifier;
    }());
    var Filter = /** @class */ (function () {
        function Filter(filterType, isStrictFilter, comparison, matchedName, matchedValue) {
            if (filterType === void 0) { filterType = FilterType.INDEX; }
            if (isStrictFilter === void 0) { isStrictFilter = true; }
            if (comparison === void 0) { comparison = Comparison.SIMPLE; }
            this.filterType = filterType;
            this.isStrictFilter = isStrictFilter;
            this.comparison = comparison;
            this.matchedName = matchedName;
            this.matchedValue = matchedValue;
            this.joinType = JoinType.AND;
        }
        Filter.prototype.isIndexFilter = function () {
            return FilterType.INDEX === this.filterType;
        };
        Filter.prototype.isChildIndexFilter = function () {
            return FilterType.CHILD_INDEX === this.filterType;
        };
        Filter.prototype.isTextFilter = function () {
            return FilterType.TEXT === this.filterType;
        };
        Filter.prototype.isAttributeFilter = function () {
            return FilterType.ATTRIBUTE === this.filterType;
        };
        Filter.prototype.isDataFilter = function () {
            return FilterType.DATA === this.filterType;
        };
        return Filter;
    }());
    var BBSeekerError = /** @class */ (function (_super) {
        __extends(BBSeekerError, _super);
        function BBSeekerError(message, type) {
            var _this = _super.call(this, message) || this;
            _this.message = message;
            _this.type = type;
            _this.name = "BBSeekerError";
            _this["__proto__"] = BBSeekerError.prototype; // vyzkouset jestli to bude vypisovat spravny errory kdyz to vyhodim
            _this.formatMessage();
            return _this;
        }
        BBSeekerError.prototype.formatMessage = function () {
            this.message = this.name + "(" + ErrorType[this.type] + "): " + this.message;
        };
        return BBSeekerError;
    }(Error));
    var FilterType;
    (function (FilterType) {
        FilterType[FilterType["INDEX"] = 0] = "INDEX";
        FilterType[FilterType["CHILD_INDEX"] = 1] = "CHILD_INDEX";
        FilterType[FilterType["TEXT"] = 2] = "TEXT";
        FilterType[FilterType["ATTRIBUTE"] = 3] = "ATTRIBUTE";
        FilterType[FilterType["DATA"] = 4] = "DATA";
    })(FilterType || (FilterType = {}));
    var ErrorType;
    (function (ErrorType) {
        ErrorType[ErrorType["BOBRIL"] = 0] = "BOBRIL";
        ErrorType[ErrorType["PARSER"] = 1] = "PARSER";
        ErrorType[ErrorType["SEARCH"] = 2] = "SEARCH";
        ErrorType[ErrorType["TIMEOUT"] = 3] = "TIMEOUT";
    })(ErrorType || (ErrorType = {}));
    var JoinType;
    (function (JoinType) {
        JoinType[JoinType["AND"] = 0] = "AND";
        JoinType[JoinType["OR"] = 1] = "OR";
    })(JoinType || (JoinType = {}));
    var Comparison;
    (function (Comparison) {
        Comparison[Comparison["SIMPLE"] = 0] = "SIMPLE";
        Comparison[Comparison["STARTS_WITH"] = 1] = "STARTS_WITH";
        Comparison[Comparison["ENDS_WITH"] = 2] = "ENDS_WITH";
        Comparison[Comparison["COMPLEX"] = 3] = "COMPLEX";
    })(Comparison || (Comparison = {}));
    var Strictness;
    (function (Strictness) {
        Strictness[Strictness["NOT_VALID"] = 0] = "NOT_VALID";
        Strictness[Strictness["STRICT"] = 1] = "STRICT";
        Strictness[Strictness["NON_STRICT"] = 2] = "NON_STRICT";
    })(Strictness || (Strictness = {}));
    function clone(o) {
        var cloneObj = new o.constructor();
        for (var attr in o) {
            if (typeof o[attr] === "object") {
                cloneObj[attr] = clone(o[attr]);
            }
            else {
                cloneObj[attr] = o[attr];
            }
        }
        return cloneObj;
    }
    window["BBSeeker"] = BBSeeker;
})(BBSeeker || (BBSeeker = {}));
