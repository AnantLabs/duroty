
/*
 * Copyright 2006 Open Source Applications Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
function Hash(arguments) {
    this.length = 0;
    this.items = []; // Hash keys and their values
    this.order = []; // Array of the order of hash keys
    this.cursorPos = 0; // Current cursor position in the hash
    if (arguments != null) {
        for (var i = 0; i < arguments.length; i += 2) {
            if (typeof (arguments[i + 1]) != "undefined") {
                this.items[arguments[i]] = arguments[i + 1];
                this.order[this.length] = arguments[i];
                this.length++;
            }
        }
    }
    this.getItem = function (key) {
        return this.items[key];
    };
    this.setItem = function (key, val) {
        if (typeof val != "undefined") {
            if (typeof this.items[key] == "undefined") {
                this.order[this.length] = key;
                this.length++;
            }
            this.items[key] = val;
            return this.items[key];
        }
    };
    this.addItem = function (key, val) {
        this.setItem(key, val);
    };
    this.removeItem = function (key) {
        if (typeof this.items[key] != "undefined") {
            var pos = null;
            delete this.items[key]; // Remove the value
            // Find the key in the order list
            for (var i = 0; i < this.order.length; i++) {
                if (this.order[i] == key) {
                    pos = i;
                }
            }
            this.order.splice(pos, 1); // Remove the key
            this.length--; // Decrement the length
        }
    };
    this.hasKey = function (key) {
        return typeof this.items[key] != "undefined";
    };
    this.hasValue = function (val) {
        for (var i = 0; i < this.order.length; i++) {
            if (this.items[this.order[i]] == val) {
                return true;
            }
        }
        return false;
    };
    this.allKeys = function (str) {
        return this.order.join(str);
    };
    this.replaceKey = function (oldKey, newKey) {
        // If item for newKey exists, nuke it
        if (this.hasKey(newKey)) {
            this.removeItem(newKey);
        }
        this.items[newKey] = this.items[oldKey];
        delete this.items[oldKey];
        for (var i = 0; i < this.order.length; i++) {
            if (this.order[i] == oldKey) {
                this.order[i] = newKey;
            }
        }
    };
    this.getAtPos = function (pos) {
        var lookup = this.items[this.order[pos]];
        return typeof lookup != "undefined" ? lookup : false;
    };
    this.removeAtPos = function (pos) {
        var ret = this.items[this.order[pos]];
        if (typeof ret != "undefined") {
            delete this.items[this.order[pos]];
            this.order.splice(pos, 1);
            this.length--;
            return true;
        } else {
            return false;
        }
    };
    this.getFirst = function () {
        return this.items[this.order[0]];
    };
    this.getLast = function () {
        return this.items[this.order[this.length - 1]];
    };
    this.getCurrent = function () {
        return this.items[this.order[this.cursorPos]];
    };
    this.getNext = function () {
        if (this.cursorPos == this.length - 1) {
            return false;
        } else {
            this.cursorPos++;
            return this.items[this.order[this.cursorPos]];
        }
    };
    this.getPrevious = function () {
        if (this.cursorPos == 0) {
            return false;
        } else {
            this.cursorPos--;
            return this.items[this.order[this.cursorPos]];
        }
    };
    this.pop = function () {
        var pos = this.length - 1;
        var ret = this.items[this.order[pos]];
        if (typeof ret != "undefined") {
            this.removeAtPos(pos);
            return ret;
        } else {
            return false;
        }
    };
    this.set = function (cursorPos) {
        this.cursorPos = cursorPos;
    };
    this.reset = function () {
        this.cursorPos = 0;
    };
    this.end = function () {
        this.cursorPos = (this.length - 1);
    };
    this.each = function (func) {
        for (var i = 0; i < this.order.length; i++) {
            var key = this.order[i];
            var val = this.items[key];
            func(key, val);
        }
        return true;
    };
    this.sort = function (specialSort, desc) {
        var sortFunc = this.getSort(specialSort, desc);
        var valSort = [];
        var keySort = [];
        for (var i = 0; i < this.order.length; i++) {
            valSort[i] = this.items[this.order[i]];
        }
        // Sort values
        valSort.sort(sortFunc);
        for (var i = 0; i < valSort.length; i++) {
            for (j in this.items) {
                if (this.items[j] == valSort[i]) {
                    keySort[i] = j;
                    this.removeItem(j);
                }
            }
        }
        for (var i = 0; i < valSort.length; i++) {
            this.sort[i] = keySort[i];
            this.setItem(keySort[i], valSort[i]);
        }
    };
    this.sortByKey = function (specialSort, desc) {
        var sortFunc = this.getSort(specialSort, desc);
        this.order.sort(sortFunc);
    };
    // Sort methods
    // ==============
    this.getSort = function (specialSort, desc) {
        var sortFunc = null;
        if (typeof specialSort == "function") {
            sortFunc = specialSort;
        } else {
            if (specialSort == true) {
                sortFunc = desc ? this.simpleDescNoCase : this.simpleAscNoCase;
            } else {
                sortFunc = desc ? this.simpleDescCase : this.simpleAscCase;
            }
        }
        return sortFunc;
    };
    this.simpleAscCase = function (a, b) {
        return (a >= b) ? 1 : -1;
    };
    this.simpleDescCase = function (a, b) {
        return (a < b) ? 1 : -1;
    };
    this.simpleAscNoCase = function (a, b) {
        return (a.toLowerCase() >= b.toLowerCase()) ? 1 : -1;
    };
    this.simpleDescNoCase = function (a, b) {
        return (a.toLowerCase() < b.toLowerCase()) ? 1 : -1;
    };
}

