/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	// The require scope
/******/ 	var __webpack_require__ = {};
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/define property getters */
/******/ 	(() => {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = (exports, definition) => {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	(() => {
/******/ 		__webpack_require__.o = (obj, prop) => (Object.prototype.hasOwnProperty.call(obj, prop))
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};

// NAMESPACE OBJECT: ./node_modules/axios/lib/platform/common/utils.js
var common_utils_namespaceObject = {};
__webpack_require__.r(common_utils_namespaceObject);
__webpack_require__.d(common_utils_namespaceObject, {
  hasBrowserEnv: () => (hasBrowserEnv),
  hasStandardBrowserEnv: () => (hasStandardBrowserEnv),
  hasStandardBrowserWebWorkerEnv: () => (hasStandardBrowserWebWorkerEnv)
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/bind.js


function bind(fn, thisArg) {
  return function wrap() {
    return fn.apply(thisArg, arguments);
  };
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/utils.js




// utils is a library of generic helper functions non-specific to axios

const {toString: utils_toString} = Object.prototype;
const {getPrototypeOf} = Object;

const kindOf = (cache => thing => {
    const str = utils_toString.call(thing);
    return cache[str] || (cache[str] = str.slice(8, -1).toLowerCase());
})(Object.create(null));

const kindOfTest = (type) => {
  type = type.toLowerCase();
  return (thing) => kindOf(thing) === type
}

const typeOfTest = type => thing => typeof thing === type;

/**
 * Determine if a value is an Array
 *
 * @param {Object} val The value to test
 *
 * @returns {boolean} True if value is an Array, otherwise false
 */
const {isArray} = Array;

/**
 * Determine if a value is undefined
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if the value is undefined, otherwise false
 */
const isUndefined = typeOfTest('undefined');

/**
 * Determine if a value is a Buffer
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a Buffer, otherwise false
 */
function isBuffer(val) {
  return val !== null && !isUndefined(val) && val.constructor !== null && !isUndefined(val.constructor)
    && isFunction(val.constructor.isBuffer) && val.constructor.isBuffer(val);
}

/**
 * Determine if a value is an ArrayBuffer
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is an ArrayBuffer, otherwise false
 */
const isArrayBuffer = kindOfTest('ArrayBuffer');


/**
 * Determine if a value is a view on an ArrayBuffer
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a view on an ArrayBuffer, otherwise false
 */
function isArrayBufferView(val) {
  let result;
  if ((typeof ArrayBuffer !== 'undefined') && (ArrayBuffer.isView)) {
    result = ArrayBuffer.isView(val);
  } else {
    result = (val) && (val.buffer) && (isArrayBuffer(val.buffer));
  }
  return result;
}

/**
 * Determine if a value is a String
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a String, otherwise false
 */
const isString = typeOfTest('string');

/**
 * Determine if a value is a Function
 *
 * @param {*} val The value to test
 * @returns {boolean} True if value is a Function, otherwise false
 */
const isFunction = typeOfTest('function');

/**
 * Determine if a value is a Number
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a Number, otherwise false
 */
const isNumber = typeOfTest('number');

/**
 * Determine if a value is an Object
 *
 * @param {*} thing The value to test
 *
 * @returns {boolean} True if value is an Object, otherwise false
 */
const isObject = (thing) => thing !== null && typeof thing === 'object';

/**
 * Determine if a value is a Boolean
 *
 * @param {*} thing The value to test
 * @returns {boolean} True if value is a Boolean, otherwise false
 */
const isBoolean = thing => thing === true || thing === false;

/**
 * Determine if a value is a plain Object
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a plain Object, otherwise false
 */
const isPlainObject = (val) => {
  if (kindOf(val) !== 'object') {
    return false;
  }

  const prototype = getPrototypeOf(val);
  return (prototype === null || prototype === Object.prototype || Object.getPrototypeOf(prototype) === null) && !(Symbol.toStringTag in val) && !(Symbol.iterator in val);
}

/**
 * Determine if a value is a Date
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a Date, otherwise false
 */
const isDate = kindOfTest('Date');

/**
 * Determine if a value is a File
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a File, otherwise false
 */
const isFile = kindOfTest('File');

/**
 * Determine if a value is a Blob
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a Blob, otherwise false
 */
const isBlob = kindOfTest('Blob');

/**
 * Determine if a value is a FileList
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a File, otherwise false
 */
const isFileList = kindOfTest('FileList');

/**
 * Determine if a value is a Stream
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a Stream, otherwise false
 */
const isStream = (val) => isObject(val) && isFunction(val.pipe);

/**
 * Determine if a value is a FormData
 *
 * @param {*} thing The value to test
 *
 * @returns {boolean} True if value is an FormData, otherwise false
 */
const isFormData = (thing) => {
  let kind;
  return thing && (
    (typeof FormData === 'function' && thing instanceof FormData) || (
      isFunction(thing.append) && (
        (kind = kindOf(thing)) === 'formdata' ||
        // detect form-data instance
        (kind === 'object' && isFunction(thing.toString) && thing.toString() === '[object FormData]')
      )
    )
  )
}

/**
 * Determine if a value is a URLSearchParams object
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a URLSearchParams object, otherwise false
 */
const isURLSearchParams = kindOfTest('URLSearchParams');

/**
 * Trim excess whitespace off the beginning and end of a string
 *
 * @param {String} str The String to trim
 *
 * @returns {String} The String freed of excess whitespace
 */
const trim = (str) => str.trim ?
  str.trim() : str.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');

/**
 * Iterate over an Array or an Object invoking a function for each item.
 *
 * If `obj` is an Array callback will be called passing
 * the value, index, and complete array for each item.
 *
 * If 'obj' is an Object callback will be called passing
 * the value, key, and complete object for each property.
 *
 * @param {Object|Array} obj The object to iterate
 * @param {Function} fn The callback to invoke for each item
 *
 * @param {Boolean} [allOwnKeys = false]
 * @returns {any}
 */
function forEach(obj, fn, {allOwnKeys = false} = {}) {
  // Don't bother if no value provided
  if (obj === null || typeof obj === 'undefined') {
    return;
  }

  let i;
  let l;

  // Force an array if not already something iterable
  if (typeof obj !== 'object') {
    /*eslint no-param-reassign:0*/
    obj = [obj];
  }

  if (isArray(obj)) {
    // Iterate over array values
    for (i = 0, l = obj.length; i < l; i++) {
      fn.call(null, obj[i], i, obj);
    }
  } else {
    // Iterate over object keys
    const keys = allOwnKeys ? Object.getOwnPropertyNames(obj) : Object.keys(obj);
    const len = keys.length;
    let key;

    for (i = 0; i < len; i++) {
      key = keys[i];
      fn.call(null, obj[key], key, obj);
    }
  }
}

function findKey(obj, key) {
  key = key.toLowerCase();
  const keys = Object.keys(obj);
  let i = keys.length;
  let _key;
  while (i-- > 0) {
    _key = keys[i];
    if (key === _key.toLowerCase()) {
      return _key;
    }
  }
  return null;
}

const _global = (() => {
  /*eslint no-undef:0*/
  if (typeof globalThis !== "undefined") return globalThis;
  return typeof self !== "undefined" ? self : (typeof window !== 'undefined' ? window : global)
})();

const isContextDefined = (context) => !isUndefined(context) && context !== _global;

/**
 * Accepts varargs expecting each argument to be an object, then
 * immutably merges the properties of each object and returns result.
 *
 * When multiple objects contain the same key the later object in
 * the arguments list will take precedence.
 *
 * Example:
 *
 * ```js
 * var result = merge({foo: 123}, {foo: 456});
 * console.log(result.foo); // outputs 456
 * ```
 *
 * @param {Object} obj1 Object to merge
 *
 * @returns {Object} Result of all merge properties
 */
function merge(/* obj1, obj2, obj3, ... */) {
  const {caseless} = isContextDefined(this) && this || {};
  const result = {};
  const assignValue = (val, key) => {
    const targetKey = caseless && findKey(result, key) || key;
    if (isPlainObject(result[targetKey]) && isPlainObject(val)) {
      result[targetKey] = merge(result[targetKey], val);
    } else if (isPlainObject(val)) {
      result[targetKey] = merge({}, val);
    } else if (isArray(val)) {
      result[targetKey] = val.slice();
    } else {
      result[targetKey] = val;
    }
  }

  for (let i = 0, l = arguments.length; i < l; i++) {
    arguments[i] && forEach(arguments[i], assignValue);
  }
  return result;
}

/**
 * Extends object a by mutably adding to it the properties of object b.
 *
 * @param {Object} a The object to be extended
 * @param {Object} b The object to copy properties from
 * @param {Object} thisArg The object to bind function to
 *
 * @param {Boolean} [allOwnKeys]
 * @returns {Object} The resulting value of object a
 */
const extend = (a, b, thisArg, {allOwnKeys}= {}) => {
  forEach(b, (val, key) => {
    if (thisArg && isFunction(val)) {
      a[key] = bind(val, thisArg);
    } else {
      a[key] = val;
    }
  }, {allOwnKeys});
  return a;
}

/**
 * Remove byte order marker. This catches EF BB BF (the UTF-8 BOM)
 *
 * @param {string} content with BOM
 *
 * @returns {string} content value without BOM
 */
const stripBOM = (content) => {
  if (content.charCodeAt(0) === 0xFEFF) {
    content = content.slice(1);
  }
  return content;
}

/**
 * Inherit the prototype methods from one constructor into another
 * @param {function} constructor
 * @param {function} superConstructor
 * @param {object} [props]
 * @param {object} [descriptors]
 *
 * @returns {void}
 */
const inherits = (constructor, superConstructor, props, descriptors) => {
  constructor.prototype = Object.create(superConstructor.prototype, descriptors);
  constructor.prototype.constructor = constructor;
  Object.defineProperty(constructor, 'super', {
    value: superConstructor.prototype
  });
  props && Object.assign(constructor.prototype, props);
}

/**
 * Resolve object with deep prototype chain to a flat object
 * @param {Object} sourceObj source object
 * @param {Object} [destObj]
 * @param {Function|Boolean} [filter]
 * @param {Function} [propFilter]
 *
 * @returns {Object}
 */
const toFlatObject = (sourceObj, destObj, filter, propFilter) => {
  let props;
  let i;
  let prop;
  const merged = {};

  destObj = destObj || {};
  // eslint-disable-next-line no-eq-null,eqeqeq
  if (sourceObj == null) return destObj;

  do {
    props = Object.getOwnPropertyNames(sourceObj);
    i = props.length;
    while (i-- > 0) {
      prop = props[i];
      if ((!propFilter || propFilter(prop, sourceObj, destObj)) && !merged[prop]) {
        destObj[prop] = sourceObj[prop];
        merged[prop] = true;
      }
    }
    sourceObj = filter !== false && getPrototypeOf(sourceObj);
  } while (sourceObj && (!filter || filter(sourceObj, destObj)) && sourceObj !== Object.prototype);

  return destObj;
}

/**
 * Determines whether a string ends with the characters of a specified string
 *
 * @param {String} str
 * @param {String} searchString
 * @param {Number} [position= 0]
 *
 * @returns {boolean}
 */
const endsWith = (str, searchString, position) => {
  str = String(str);
  if (position === undefined || position > str.length) {
    position = str.length;
  }
  position -= searchString.length;
  const lastIndex = str.indexOf(searchString, position);
  return lastIndex !== -1 && lastIndex === position;
}


/**
 * Returns new array from array like object or null if failed
 *
 * @param {*} [thing]
 *
 * @returns {?Array}
 */
const toArray = (thing) => {
  if (!thing) return null;
  if (isArray(thing)) return thing;
  let i = thing.length;
  if (!isNumber(i)) return null;
  const arr = new Array(i);
  while (i-- > 0) {
    arr[i] = thing[i];
  }
  return arr;
}

/**
 * Checking if the Uint8Array exists and if it does, it returns a function that checks if the
 * thing passed in is an instance of Uint8Array
 *
 * @param {TypedArray}
 *
 * @returns {Array}
 */
// eslint-disable-next-line func-names
const isTypedArray = (TypedArray => {
  // eslint-disable-next-line func-names
  return thing => {
    return TypedArray && thing instanceof TypedArray;
  };
})(typeof Uint8Array !== 'undefined' && getPrototypeOf(Uint8Array));

/**
 * For each entry in the object, call the function with the key and value.
 *
 * @param {Object<any, any>} obj - The object to iterate over.
 * @param {Function} fn - The function to call for each entry.
 *
 * @returns {void}
 */
const forEachEntry = (obj, fn) => {
  const generator = obj && obj[Symbol.iterator];

  const iterator = generator.call(obj);

  let result;

  while ((result = iterator.next()) && !result.done) {
    const pair = result.value;
    fn.call(obj, pair[0], pair[1]);
  }
}

/**
 * It takes a regular expression and a string, and returns an array of all the matches
 *
 * @param {string} regExp - The regular expression to match against.
 * @param {string} str - The string to search.
 *
 * @returns {Array<boolean>}
 */
const matchAll = (regExp, str) => {
  let matches;
  const arr = [];

  while ((matches = regExp.exec(str)) !== null) {
    arr.push(matches);
  }

  return arr;
}

/* Checking if the kindOfTest function returns true when passed an HTMLFormElement. */
const isHTMLForm = kindOfTest('HTMLFormElement');

const toCamelCase = str => {
  return str.toLowerCase().replace(/[-_\s]([a-z\d])(\w*)/g,
    function replacer(m, p1, p2) {
      return p1.toUpperCase() + p2;
    }
  );
};

/* Creating a function that will check if an object has a property. */
const utils_hasOwnProperty = (({hasOwnProperty}) => (obj, prop) => hasOwnProperty.call(obj, prop))(Object.prototype);

/**
 * Determine if a value is a RegExp object
 *
 * @param {*} val The value to test
 *
 * @returns {boolean} True if value is a RegExp object, otherwise false
 */
const isRegExp = kindOfTest('RegExp');

const reduceDescriptors = (obj, reducer) => {
  const descriptors = Object.getOwnPropertyDescriptors(obj);
  const reducedDescriptors = {};

  forEach(descriptors, (descriptor, name) => {
    let ret;
    if ((ret = reducer(descriptor, name, obj)) !== false) {
      reducedDescriptors[name] = ret || descriptor;
    }
  });

  Object.defineProperties(obj, reducedDescriptors);
}

/**
 * Makes all methods read-only
 * @param {Object} obj
 */

const freezeMethods = (obj) => {
  reduceDescriptors(obj, (descriptor, name) => {
    // skip restricted props in strict mode
    if (isFunction(obj) && ['arguments', 'caller', 'callee'].indexOf(name) !== -1) {
      return false;
    }

    const value = obj[name];

    if (!isFunction(value)) return;

    descriptor.enumerable = false;

    if ('writable' in descriptor) {
      descriptor.writable = false;
      return;
    }

    if (!descriptor.set) {
      descriptor.set = () => {
        throw Error('Can not rewrite read-only method \'' + name + '\'');
      };
    }
  });
}

const toObjectSet = (arrayOrString, delimiter) => {
  const obj = {};

  const define = (arr) => {
    arr.forEach(value => {
      obj[value] = true;
    });
  }

  isArray(arrayOrString) ? define(arrayOrString) : define(String(arrayOrString).split(delimiter));

  return obj;
}

const noop = () => {}

const toFiniteNumber = (value, defaultValue) => {
  value = +value;
  return Number.isFinite(value) ? value : defaultValue;
}

const ALPHA = 'abcdefghijklmnopqrstuvwxyz'

const DIGIT = '0123456789';

const ALPHABET = {
  DIGIT,
  ALPHA,
  ALPHA_DIGIT: ALPHA + ALPHA.toUpperCase() + DIGIT
}

const generateString = (size = 16, alphabet = ALPHABET.ALPHA_DIGIT) => {
  let str = '';
  const {length} = alphabet;
  while (size--) {
    str += alphabet[Math.random() * length|0]
  }

  return str;
}

/**
 * If the thing is a FormData object, return true, otherwise return false.
 *
 * @param {unknown} thing - The thing to check.
 *
 * @returns {boolean}
 */
function isSpecCompliantForm(thing) {
  return !!(thing && isFunction(thing.append) && thing[Symbol.toStringTag] === 'FormData' && thing[Symbol.iterator]);
}

const toJSONObject = (obj) => {
  const stack = new Array(10);

  const visit = (source, i) => {

    if (isObject(source)) {
      if (stack.indexOf(source) >= 0) {
        return;
      }

      if(!('toJSON' in source)) {
        stack[i] = source;
        const target = isArray(source) ? [] : {};

        forEach(source, (value, key) => {
          const reducedValue = visit(value, i + 1);
          !isUndefined(reducedValue) && (target[key] = reducedValue);
        });

        stack[i] = undefined;

        return target;
      }
    }

    return source;
  }

  return visit(obj, 0);
}

const isAsyncFn = kindOfTest('AsyncFunction');

const isThenable = (thing) =>
  thing && (isObject(thing) || isFunction(thing)) && isFunction(thing.then) && isFunction(thing.catch);

/* harmony default export */ const utils = ({
  isArray,
  isArrayBuffer,
  isBuffer,
  isFormData,
  isArrayBufferView,
  isString,
  isNumber,
  isBoolean,
  isObject,
  isPlainObject,
  isUndefined,
  isDate,
  isFile,
  isBlob,
  isRegExp,
  isFunction,
  isStream,
  isURLSearchParams,
  isTypedArray,
  isFileList,
  forEach,
  merge,
  extend,
  trim,
  stripBOM,
  inherits,
  toFlatObject,
  kindOf,
  kindOfTest,
  endsWith,
  toArray,
  forEachEntry,
  matchAll,
  isHTMLForm,
  hasOwnProperty: utils_hasOwnProperty,
  hasOwnProp: utils_hasOwnProperty, // an alias to avoid ESLint no-prototype-builtins detection
  reduceDescriptors,
  freezeMethods,
  toObjectSet,
  toCamelCase,
  noop,
  toFiniteNumber,
  findKey,
  global: _global,
  isContextDefined,
  ALPHABET,
  generateString,
  isSpecCompliantForm,
  toJSONObject,
  isAsyncFn,
  isThenable
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/AxiosError.js




/**
 * Create an Error with the specified message, config, error code, request and response.
 *
 * @param {string} message The error message.
 * @param {string} [code] The error code (for example, 'ECONNABORTED').
 * @param {Object} [config] The config.
 * @param {Object} [request] The request.
 * @param {Object} [response] The response.
 *
 * @returns {Error} The created error.
 */
function AxiosError(message, code, config, request, response) {
  Error.call(this);

  if (Error.captureStackTrace) {
    Error.captureStackTrace(this, this.constructor);
  } else {
    this.stack = (new Error()).stack;
  }

  this.message = message;
  this.name = 'AxiosError';
  code && (this.code = code);
  config && (this.config = config);
  request && (this.request = request);
  response && (this.response = response);
}

utils.inherits(AxiosError, Error, {
  toJSON: function toJSON() {
    return {
      // Standard
      message: this.message,
      name: this.name,
      // Microsoft
      description: this.description,
      number: this.number,
      // Mozilla
      fileName: this.fileName,
      lineNumber: this.lineNumber,
      columnNumber: this.columnNumber,
      stack: this.stack,
      // Axios
      config: utils.toJSONObject(this.config),
      code: this.code,
      status: this.response && this.response.status ? this.response.status : null
    };
  }
});

const AxiosError_prototype = AxiosError.prototype;
const descriptors = {};

[
  'ERR_BAD_OPTION_VALUE',
  'ERR_BAD_OPTION',
  'ECONNABORTED',
  'ETIMEDOUT',
  'ERR_NETWORK',
  'ERR_FR_TOO_MANY_REDIRECTS',
  'ERR_DEPRECATED',
  'ERR_BAD_RESPONSE',
  'ERR_BAD_REQUEST',
  'ERR_CANCELED',
  'ERR_NOT_SUPPORT',
  'ERR_INVALID_URL'
// eslint-disable-next-line func-names
].forEach(code => {
  descriptors[code] = {value: code};
});

Object.defineProperties(AxiosError, descriptors);
Object.defineProperty(AxiosError_prototype, 'isAxiosError', {value: true});

// eslint-disable-next-line func-names
AxiosError.from = (error, code, config, request, response, customProps) => {
  const axiosError = Object.create(AxiosError_prototype);

  utils.toFlatObject(error, axiosError, function filter(obj) {
    return obj !== Error.prototype;
  }, prop => {
    return prop !== 'isAxiosError';
  });

  AxiosError.call(axiosError, error.message, code, config, request, response);

  axiosError.cause = error;

  axiosError.name = error.name;

  customProps && Object.assign(axiosError, customProps);

  return axiosError;
};

/* harmony default export */ const core_AxiosError = (AxiosError);

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/null.js
// eslint-disable-next-line strict
/* harmony default export */ const helpers_null = (null);

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/toFormData.js




// temporary hotfix to avoid circular references until AxiosURLSearchParams is refactored


/**
 * Determines if the given thing is a array or js object.
 *
 * @param {string} thing - The object or array to be visited.
 *
 * @returns {boolean}
 */
function isVisitable(thing) {
  return utils.isPlainObject(thing) || utils.isArray(thing);
}

/**
 * It removes the brackets from the end of a string
 *
 * @param {string} key - The key of the parameter.
 *
 * @returns {string} the key without the brackets.
 */
function removeBrackets(key) {
  return utils.endsWith(key, '[]') ? key.slice(0, -2) : key;
}

/**
 * It takes a path, a key, and a boolean, and returns a string
 *
 * @param {string} path - The path to the current key.
 * @param {string} key - The key of the current object being iterated over.
 * @param {string} dots - If true, the key will be rendered with dots instead of brackets.
 *
 * @returns {string} The path to the current key.
 */
function renderKey(path, key, dots) {
  if (!path) return key;
  return path.concat(key).map(function each(token, i) {
    // eslint-disable-next-line no-param-reassign
    token = removeBrackets(token);
    return !dots && i ? '[' + token + ']' : token;
  }).join(dots ? '.' : '');
}

/**
 * If the array is an array and none of its elements are visitable, then it's a flat array.
 *
 * @param {Array<any>} arr - The array to check
 *
 * @returns {boolean}
 */
function isFlatArray(arr) {
  return utils.isArray(arr) && !arr.some(isVisitable);
}

const predicates = utils.toFlatObject(utils, {}, null, function filter(prop) {
  return /^is[A-Z]/.test(prop);
});

/**
 * Convert a data object to FormData
 *
 * @param {Object} obj
 * @param {?Object} [formData]
 * @param {?Object} [options]
 * @param {Function} [options.visitor]
 * @param {Boolean} [options.metaTokens = true]
 * @param {Boolean} [options.dots = false]
 * @param {?Boolean} [options.indexes = false]
 *
 * @returns {Object}
 **/

/**
 * It converts an object into a FormData object
 *
 * @param {Object<any, any>} obj - The object to convert to form data.
 * @param {string} formData - The FormData object to append to.
 * @param {Object<string, any>} options
 *
 * @returns
 */
function toFormData(obj, formData, options) {
  if (!utils.isObject(obj)) {
    throw new TypeError('target must be an object');
  }

  // eslint-disable-next-line no-param-reassign
  formData = formData || new (helpers_null || FormData)();

  // eslint-disable-next-line no-param-reassign
  options = utils.toFlatObject(options, {
    metaTokens: true,
    dots: false,
    indexes: false
  }, false, function defined(option, source) {
    // eslint-disable-next-line no-eq-null,eqeqeq
    return !utils.isUndefined(source[option]);
  });

  const metaTokens = options.metaTokens;
  // eslint-disable-next-line no-use-before-define
  const visitor = options.visitor || defaultVisitor;
  const dots = options.dots;
  const indexes = options.indexes;
  const _Blob = options.Blob || typeof Blob !== 'undefined' && Blob;
  const useBlob = _Blob && utils.isSpecCompliantForm(formData);

  if (!utils.isFunction(visitor)) {
    throw new TypeError('visitor must be a function');
  }

  function convertValue(value) {
    if (value === null) return '';

    if (utils.isDate(value)) {
      return value.toISOString();
    }

    if (!useBlob && utils.isBlob(value)) {
      throw new core_AxiosError('Blob is not supported. Use a Buffer instead.');
    }

    if (utils.isArrayBuffer(value) || utils.isTypedArray(value)) {
      return useBlob && typeof Blob === 'function' ? new Blob([value]) : Buffer.from(value);
    }

    return value;
  }

  /**
   * Default visitor.
   *
   * @param {*} value
   * @param {String|Number} key
   * @param {Array<String|Number>} path
   * @this {FormData}
   *
   * @returns {boolean} return true to visit the each prop of the value recursively
   */
  function defaultVisitor(value, key, path) {
    let arr = value;

    if (value && !path && typeof value === 'object') {
      if (utils.endsWith(key, '{}')) {
        // eslint-disable-next-line no-param-reassign
        key = metaTokens ? key : key.slice(0, -2);
        // eslint-disable-next-line no-param-reassign
        value = JSON.stringify(value);
      } else if (
        (utils.isArray(value) && isFlatArray(value)) ||
        ((utils.isFileList(value) || utils.endsWith(key, '[]')) && (arr = utils.toArray(value))
        )) {
        // eslint-disable-next-line no-param-reassign
        key = removeBrackets(key);

        arr.forEach(function each(el, index) {
          !(utils.isUndefined(el) || el === null) && formData.append(
            // eslint-disable-next-line no-nested-ternary
            indexes === true ? renderKey([key], index, dots) : (indexes === null ? key : key + '[]'),
            convertValue(el)
          );
        });
        return false;
      }
    }

    if (isVisitable(value)) {
      return true;
    }

    formData.append(renderKey(path, key, dots), convertValue(value));

    return false;
  }

  const stack = [];

  const exposedHelpers = Object.assign(predicates, {
    defaultVisitor,
    convertValue,
    isVisitable
  });

  function build(value, path) {
    if (utils.isUndefined(value)) return;

    if (stack.indexOf(value) !== -1) {
      throw Error('Circular reference detected in ' + path.join('.'));
    }

    stack.push(value);

    utils.forEach(value, function each(el, key) {
      const result = !(utils.isUndefined(el) || el === null) && visitor.call(
        formData, el, utils.isString(key) ? key.trim() : key, path, exposedHelpers
      );

      if (result === true) {
        build(el, path ? path.concat(key) : [key]);
      }
    });

    stack.pop();
  }

  if (!utils.isObject(obj)) {
    throw new TypeError('data must be an object');
  }

  build(obj);

  return formData;
}

/* harmony default export */ const helpers_toFormData = (toFormData);

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/AxiosURLSearchParams.js




/**
 * It encodes a string by replacing all characters that are not in the unreserved set with
 * their percent-encoded equivalents
 *
 * @param {string} str - The string to encode.
 *
 * @returns {string} The encoded string.
 */
function encode(str) {
  const charMap = {
    '!': '%21',
    "'": '%27',
    '(': '%28',
    ')': '%29',
    '~': '%7E',
    '%20': '+',
    '%00': '\x00'
  };
  return encodeURIComponent(str).replace(/[!'()~]|%20|%00/g, function replacer(match) {
    return charMap[match];
  });
}

/**
 * It takes a params object and converts it to a FormData object
 *
 * @param {Object<string, any>} params - The parameters to be converted to a FormData object.
 * @param {Object<string, any>} options - The options object passed to the Axios constructor.
 *
 * @returns {void}
 */
function AxiosURLSearchParams(params, options) {
  this._pairs = [];

  params && helpers_toFormData(params, this, options);
}

const AxiosURLSearchParams_prototype = AxiosURLSearchParams.prototype;

AxiosURLSearchParams_prototype.append = function append(name, value) {
  this._pairs.push([name, value]);
};

AxiosURLSearchParams_prototype.toString = function toString(encoder) {
  const _encode = encoder ? function(value) {
    return encoder.call(this, value, encode);
  } : encode;

  return this._pairs.map(function each(pair) {
    return _encode(pair[0]) + '=' + _encode(pair[1]);
  }, '').join('&');
};

/* harmony default export */ const helpers_AxiosURLSearchParams = (AxiosURLSearchParams);

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/buildURL.js





/**
 * It replaces all instances of the characters `:`, `$`, `,`, `+`, `[`, and `]` with their
 * URI encoded counterparts
 *
 * @param {string} val The value to be encoded.
 *
 * @returns {string} The encoded value.
 */
function buildURL_encode(val) {
  return encodeURIComponent(val).
    replace(/%3A/gi, ':').
    replace(/%24/g, '$').
    replace(/%2C/gi, ',').
    replace(/%20/g, '+').
    replace(/%5B/gi, '[').
    replace(/%5D/gi, ']');
}

/**
 * Build a URL by appending params to the end
 *
 * @param {string} url The base of the url (e.g., http://www.google.com)
 * @param {object} [params] The params to be appended
 * @param {?object} options
 *
 * @returns {string} The formatted url
 */
function buildURL(url, params, options) {
  /*eslint no-param-reassign:0*/
  if (!params) {
    return url;
  }
  
  const _encode = options && options.encode || buildURL_encode;

  const serializeFn = options && options.serialize;

  let serializedParams;

  if (serializeFn) {
    serializedParams = serializeFn(params, options);
  } else {
    serializedParams = utils.isURLSearchParams(params) ?
      params.toString() :
      new helpers_AxiosURLSearchParams(params, options).toString(_encode);
  }

  if (serializedParams) {
    const hashmarkIndex = url.indexOf("#");

    if (hashmarkIndex !== -1) {
      url = url.slice(0, hashmarkIndex);
    }
    url += (url.indexOf('?') === -1 ? '?' : '&') + serializedParams;
  }

  return url;
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/InterceptorManager.js




class InterceptorManager {
  constructor() {
    this.handlers = [];
  }

  /**
   * Add a new interceptor to the stack
   *
   * @param {Function} fulfilled The function to handle `then` for a `Promise`
   * @param {Function} rejected The function to handle `reject` for a `Promise`
   *
   * @return {Number} An ID used to remove interceptor later
   */
  use(fulfilled, rejected, options) {
    this.handlers.push({
      fulfilled,
      rejected,
      synchronous: options ? options.synchronous : false,
      runWhen: options ? options.runWhen : null
    });
    return this.handlers.length - 1;
  }

  /**
   * Remove an interceptor from the stack
   *
   * @param {Number} id The ID that was returned by `use`
   *
   * @returns {Boolean} `true` if the interceptor was removed, `false` otherwise
   */
  eject(id) {
    if (this.handlers[id]) {
      this.handlers[id] = null;
    }
  }

  /**
   * Clear all interceptors from the stack
   *
   * @returns {void}
   */
  clear() {
    if (this.handlers) {
      this.handlers = [];
    }
  }

  /**
   * Iterate over all the registered interceptors
   *
   * This method is particularly useful for skipping over any
   * interceptors that may have become `null` calling `eject`.
   *
   * @param {Function} fn The function to call for each interceptor
   *
   * @returns {void}
   */
  forEach(fn) {
    utils.forEach(this.handlers, function forEachHandler(h) {
      if (h !== null) {
        fn(h);
      }
    });
  }
}

/* harmony default export */ const core_InterceptorManager = (InterceptorManager);

;// CONCATENATED MODULE: ./node_modules/axios/lib/defaults/transitional.js


/* harmony default export */ const defaults_transitional = ({
  silentJSONParsing: true,
  forcedJSONParsing: true,
  clarifyTimeoutError: false
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/platform/browser/classes/URLSearchParams.js



/* harmony default export */ const classes_URLSearchParams = (typeof URLSearchParams !== 'undefined' ? URLSearchParams : helpers_AxiosURLSearchParams);

;// CONCATENATED MODULE: ./node_modules/axios/lib/platform/browser/classes/FormData.js


/* harmony default export */ const classes_FormData = (typeof FormData !== 'undefined' ? FormData : null);

;// CONCATENATED MODULE: ./node_modules/axios/lib/platform/browser/classes/Blob.js


/* harmony default export */ const classes_Blob = (typeof Blob !== 'undefined' ? Blob : null);

;// CONCATENATED MODULE: ./node_modules/axios/lib/platform/browser/index.js




/* harmony default export */ const browser = ({
  isBrowser: true,
  classes: {
    URLSearchParams: classes_URLSearchParams,
    FormData: classes_FormData,
    Blob: classes_Blob
  },
  protocols: ['http', 'https', 'file', 'blob', 'url', 'data']
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/platform/common/utils.js
const hasBrowserEnv = typeof window !== 'undefined' && typeof document !== 'undefined';

/**
 * Determine if we're running in a standard browser environment
 *
 * This allows axios to run in a web worker, and react-native.
 * Both environments support XMLHttpRequest, but not fully standard globals.
 *
 * web workers:
 *  typeof window -> undefined
 *  typeof document -> undefined
 *
 * react-native:
 *  navigator.product -> 'ReactNative'
 * nativescript
 *  navigator.product -> 'NativeScript' or 'NS'
 *
 * @returns {boolean}
 */
const hasStandardBrowserEnv = (
  (product) => {
    return hasBrowserEnv && ['ReactNative', 'NativeScript', 'NS'].indexOf(product) < 0
  })(typeof navigator !== 'undefined' && navigator.product);

/**
 * Determine if we're running in a standard browser webWorker environment
 *
 * Although the `isStandardBrowserEnv` method indicates that
 * `allows axios to run in a web worker`, the WebWorker will still be
 * filtered out due to its judgment standard
 * `typeof window !== 'undefined' && typeof document !== 'undefined'`.
 * This leads to a problem when axios post `FormData` in webWorker
 */
const hasStandardBrowserWebWorkerEnv = (() => {
  return (
    typeof WorkerGlobalScope !== 'undefined' &&
    // eslint-disable-next-line no-undef
    self instanceof WorkerGlobalScope &&
    typeof self.importScripts === 'function'
  );
})();



;// CONCATENATED MODULE: ./node_modules/axios/lib/platform/index.js



/* harmony default export */ const platform = ({
  ...common_utils_namespaceObject,
  ...browser
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/toURLEncodedForm.js






function toURLEncodedForm(data, options) {
  return helpers_toFormData(data, new platform.classes.URLSearchParams(), Object.assign({
    visitor: function(value, key, path, helpers) {
      if (platform.isNode && utils.isBuffer(value)) {
        this.append(key, value.toString('base64'));
        return false;
      }

      return helpers.defaultVisitor.apply(this, arguments);
    }
  }, options));
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/formDataToJSON.js




/**
 * It takes a string like `foo[x][y][z]` and returns an array like `['foo', 'x', 'y', 'z']
 *
 * @param {string} name - The name of the property to get.
 *
 * @returns An array of strings.
 */
function parsePropPath(name) {
  // foo[x][y][z]
  // foo.x.y.z
  // foo-x-y-z
  // foo x y z
  return utils.matchAll(/\w+|\[(\w*)]/g, name).map(match => {
    return match[0] === '[]' ? '' : match[1] || match[0];
  });
}

/**
 * Convert an array to an object.
 *
 * @param {Array<any>} arr - The array to convert to an object.
 *
 * @returns An object with the same keys and values as the array.
 */
function arrayToObject(arr) {
  const obj = {};
  const keys = Object.keys(arr);
  let i;
  const len = keys.length;
  let key;
  for (i = 0; i < len; i++) {
    key = keys[i];
    obj[key] = arr[key];
  }
  return obj;
}

/**
 * It takes a FormData object and returns a JavaScript object
 *
 * @param {string} formData The FormData object to convert to JSON.
 *
 * @returns {Object<string, any> | null} The converted object.
 */
function formDataToJSON(formData) {
  function buildPath(path, value, target, index) {
    let name = path[index++];
    const isNumericKey = Number.isFinite(+name);
    const isLast = index >= path.length;
    name = !name && utils.isArray(target) ? target.length : name;

    if (isLast) {
      if (utils.hasOwnProp(target, name)) {
        target[name] = [target[name], value];
      } else {
        target[name] = value;
      }

      return !isNumericKey;
    }

    if (!target[name] || !utils.isObject(target[name])) {
      target[name] = [];
    }

    const result = buildPath(path, value, target[name], index);

    if (result && utils.isArray(target[name])) {
      target[name] = arrayToObject(target[name]);
    }

    return !isNumericKey;
  }

  if (utils.isFormData(formData) && utils.isFunction(formData.entries)) {
    const obj = {};

    utils.forEachEntry(formData, (name, value) => {
      buildPath(parsePropPath(name), value, obj, 0);
    });

    return obj;
  }

  return null;
}

/* harmony default export */ const helpers_formDataToJSON = (formDataToJSON);

;// CONCATENATED MODULE: ./node_modules/axios/lib/defaults/index.js










/**
 * It takes a string, tries to parse it, and if it fails, it returns the stringified version
 * of the input
 *
 * @param {any} rawValue - The value to be stringified.
 * @param {Function} parser - A function that parses a string into a JavaScript object.
 * @param {Function} encoder - A function that takes a value and returns a string.
 *
 * @returns {string} A stringified version of the rawValue.
 */
function stringifySafely(rawValue, parser, encoder) {
  if (utils.isString(rawValue)) {
    try {
      (parser || JSON.parse)(rawValue);
      return utils.trim(rawValue);
    } catch (e) {
      if (e.name !== 'SyntaxError') {
        throw e;
      }
    }
  }

  return (encoder || JSON.stringify)(rawValue);
}

const defaults = {

  transitional: defaults_transitional,

  adapter: ['xhr', 'http'],

  transformRequest: [function transformRequest(data, headers) {
    const contentType = headers.getContentType() || '';
    const hasJSONContentType = contentType.indexOf('application/json') > -1;
    const isObjectPayload = utils.isObject(data);

    if (isObjectPayload && utils.isHTMLForm(data)) {
      data = new FormData(data);
    }

    const isFormData = utils.isFormData(data);

    if (isFormData) {
      if (!hasJSONContentType) {
        return data;
      }
      return hasJSONContentType ? JSON.stringify(helpers_formDataToJSON(data)) : data;
    }

    if (utils.isArrayBuffer(data) ||
      utils.isBuffer(data) ||
      utils.isStream(data) ||
      utils.isFile(data) ||
      utils.isBlob(data)
    ) {
      return data;
    }
    if (utils.isArrayBufferView(data)) {
      return data.buffer;
    }
    if (utils.isURLSearchParams(data)) {
      headers.setContentType('application/x-www-form-urlencoded;charset=utf-8', false);
      return data.toString();
    }

    let isFileList;

    if (isObjectPayload) {
      if (contentType.indexOf('application/x-www-form-urlencoded') > -1) {
        return toURLEncodedForm(data, this.formSerializer).toString();
      }

      if ((isFileList = utils.isFileList(data)) || contentType.indexOf('multipart/form-data') > -1) {
        const _FormData = this.env && this.env.FormData;

        return helpers_toFormData(
          isFileList ? {'files[]': data} : data,
          _FormData && new _FormData(),
          this.formSerializer
        );
      }
    }

    if (isObjectPayload || hasJSONContentType ) {
      headers.setContentType('application/json', false);
      return stringifySafely(data);
    }

    return data;
  }],

  transformResponse: [function transformResponse(data) {
    const transitional = this.transitional || defaults.transitional;
    const forcedJSONParsing = transitional && transitional.forcedJSONParsing;
    const JSONRequested = this.responseType === 'json';

    if (data && utils.isString(data) && ((forcedJSONParsing && !this.responseType) || JSONRequested)) {
      const silentJSONParsing = transitional && transitional.silentJSONParsing;
      const strictJSONParsing = !silentJSONParsing && JSONRequested;

      try {
        return JSON.parse(data);
      } catch (e) {
        if (strictJSONParsing) {
          if (e.name === 'SyntaxError') {
            throw core_AxiosError.from(e, core_AxiosError.ERR_BAD_RESPONSE, this, null, this.response);
          }
          throw e;
        }
      }
    }

    return data;
  }],

  /**
   * A timeout in milliseconds to abort a request. If set to 0 (default) a
   * timeout is not created.
   */
  timeout: 0,

  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN',

  maxContentLength: -1,
  maxBodyLength: -1,

  env: {
    FormData: platform.classes.FormData,
    Blob: platform.classes.Blob
  },

  validateStatus: function validateStatus(status) {
    return status >= 200 && status < 300;
  },

  headers: {
    common: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': undefined
    }
  }
};

utils.forEach(['delete', 'get', 'head', 'post', 'put', 'patch'], (method) => {
  defaults.headers[method] = {};
});

/* harmony default export */ const lib_defaults = (defaults);

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/parseHeaders.js




// RawAxiosHeaders whose duplicates are ignored by node
// c.f. https://nodejs.org/api/http.html#http_message_headers
const ignoreDuplicateOf = utils.toObjectSet([
  'age', 'authorization', 'content-length', 'content-type', 'etag',
  'expires', 'from', 'host', 'if-modified-since', 'if-unmodified-since',
  'last-modified', 'location', 'max-forwards', 'proxy-authorization',
  'referer', 'retry-after', 'user-agent'
]);

/**
 * Parse headers into an object
 *
 * ```
 * Date: Wed, 27 Aug 2014 08:58:49 GMT
 * Content-Type: application/json
 * Connection: keep-alive
 * Transfer-Encoding: chunked
 * ```
 *
 * @param {String} rawHeaders Headers needing to be parsed
 *
 * @returns {Object} Headers parsed into an object
 */
/* harmony default export */ const parseHeaders = (rawHeaders => {
  const parsed = {};
  let key;
  let val;
  let i;

  rawHeaders && rawHeaders.split('\n').forEach(function parser(line) {
    i = line.indexOf(':');
    key = line.substring(0, i).trim().toLowerCase();
    val = line.substring(i + 1).trim();

    if (!key || (parsed[key] && ignoreDuplicateOf[key])) {
      return;
    }

    if (key === 'set-cookie') {
      if (parsed[key]) {
        parsed[key].push(val);
      } else {
        parsed[key] = [val];
      }
    } else {
      parsed[key] = parsed[key] ? parsed[key] + ', ' + val : val;
    }
  });

  return parsed;
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/AxiosHeaders.js





const $internals = Symbol('internals');

function normalizeHeader(header) {
  return header && String(header).trim().toLowerCase();
}

function normalizeValue(value) {
  if (value === false || value == null) {
    return value;
  }

  return utils.isArray(value) ? value.map(normalizeValue) : String(value);
}

function parseTokens(str) {
  const tokens = Object.create(null);
  const tokensRE = /([^\s,;=]+)\s*(?:=\s*([^,;]+))?/g;
  let match;

  while ((match = tokensRE.exec(str))) {
    tokens[match[1]] = match[2];
  }

  return tokens;
}

const isValidHeaderName = (str) => /^[-_a-zA-Z0-9^`|~,!#$%&'*+.]+$/.test(str.trim());

function matchHeaderValue(context, value, header, filter, isHeaderNameFilter) {
  if (utils.isFunction(filter)) {
    return filter.call(this, value, header);
  }

  if (isHeaderNameFilter) {
    value = header;
  }

  if (!utils.isString(value)) return;

  if (utils.isString(filter)) {
    return value.indexOf(filter) !== -1;
  }

  if (utils.isRegExp(filter)) {
    return filter.test(value);
  }
}

function formatHeader(header) {
  return header.trim()
    .toLowerCase().replace(/([a-z\d])(\w*)/g, (w, char, str) => {
      return char.toUpperCase() + str;
    });
}

function buildAccessors(obj, header) {
  const accessorName = utils.toCamelCase(' ' + header);

  ['get', 'set', 'has'].forEach(methodName => {
    Object.defineProperty(obj, methodName + accessorName, {
      value: function(arg1, arg2, arg3) {
        return this[methodName].call(this, header, arg1, arg2, arg3);
      },
      configurable: true
    });
  });
}

class AxiosHeaders {
  constructor(headers) {
    headers && this.set(headers);
  }

  set(header, valueOrRewrite, rewrite) {
    const self = this;

    function setHeader(_value, _header, _rewrite) {
      const lHeader = normalizeHeader(_header);

      if (!lHeader) {
        throw new Error('header name must be a non-empty string');
      }

      const key = utils.findKey(self, lHeader);

      if(!key || self[key] === undefined || _rewrite === true || (_rewrite === undefined && self[key] !== false)) {
        self[key || _header] = normalizeValue(_value);
      }
    }

    const setHeaders = (headers, _rewrite) =>
      utils.forEach(headers, (_value, _header) => setHeader(_value, _header, _rewrite));

    if (utils.isPlainObject(header) || header instanceof this.constructor) {
      setHeaders(header, valueOrRewrite)
    } else if(utils.isString(header) && (header = header.trim()) && !isValidHeaderName(header)) {
      setHeaders(parseHeaders(header), valueOrRewrite);
    } else {
      header != null && setHeader(valueOrRewrite, header, rewrite);
    }

    return this;
  }

  get(header, parser) {
    header = normalizeHeader(header);

    if (header) {
      const key = utils.findKey(this, header);

      if (key) {
        const value = this[key];

        if (!parser) {
          return value;
        }

        if (parser === true) {
          return parseTokens(value);
        }

        if (utils.isFunction(parser)) {
          return parser.call(this, value, key);
        }

        if (utils.isRegExp(parser)) {
          return parser.exec(value);
        }

        throw new TypeError('parser must be boolean|regexp|function');
      }
    }
  }

  has(header, matcher) {
    header = normalizeHeader(header);

    if (header) {
      const key = utils.findKey(this, header);

      return !!(key && this[key] !== undefined && (!matcher || matchHeaderValue(this, this[key], key, matcher)));
    }

    return false;
  }

  delete(header, matcher) {
    const self = this;
    let deleted = false;

    function deleteHeader(_header) {
      _header = normalizeHeader(_header);

      if (_header) {
        const key = utils.findKey(self, _header);

        if (key && (!matcher || matchHeaderValue(self, self[key], key, matcher))) {
          delete self[key];

          deleted = true;
        }
      }
    }

    if (utils.isArray(header)) {
      header.forEach(deleteHeader);
    } else {
      deleteHeader(header);
    }

    return deleted;
  }

  clear(matcher) {
    const keys = Object.keys(this);
    let i = keys.length;
    let deleted = false;

    while (i--) {
      const key = keys[i];
      if(!matcher || matchHeaderValue(this, this[key], key, matcher, true)) {
        delete this[key];
        deleted = true;
      }
    }

    return deleted;
  }

  normalize(format) {
    const self = this;
    const headers = {};

    utils.forEach(this, (value, header) => {
      const key = utils.findKey(headers, header);

      if (key) {
        self[key] = normalizeValue(value);
        delete self[header];
        return;
      }

      const normalized = format ? formatHeader(header) : String(header).trim();

      if (normalized !== header) {
        delete self[header];
      }

      self[normalized] = normalizeValue(value);

      headers[normalized] = true;
    });

    return this;
  }

  concat(...targets) {
    return this.constructor.concat(this, ...targets);
  }

  toJSON(asStrings) {
    const obj = Object.create(null);

    utils.forEach(this, (value, header) => {
      value != null && value !== false && (obj[header] = asStrings && utils.isArray(value) ? value.join(', ') : value);
    });

    return obj;
  }

  [Symbol.iterator]() {
    return Object.entries(this.toJSON())[Symbol.iterator]();
  }

  toString() {
    return Object.entries(this.toJSON()).map(([header, value]) => header + ': ' + value).join('\n');
  }

  get [Symbol.toStringTag]() {
    return 'AxiosHeaders';
  }

  static from(thing) {
    return thing instanceof this ? thing : new this(thing);
  }

  static concat(first, ...targets) {
    const computed = new this(first);

    targets.forEach((target) => computed.set(target));

    return computed;
  }

  static accessor(header) {
    const internals = this[$internals] = (this[$internals] = {
      accessors: {}
    });

    const accessors = internals.accessors;
    const prototype = this.prototype;

    function defineAccessor(_header) {
      const lHeader = normalizeHeader(_header);

      if (!accessors[lHeader]) {
        buildAccessors(prototype, _header);
        accessors[lHeader] = true;
      }
    }

    utils.isArray(header) ? header.forEach(defineAccessor) : defineAccessor(header);

    return this;
  }
}

AxiosHeaders.accessor(['Content-Type', 'Content-Length', 'Accept', 'Accept-Encoding', 'User-Agent', 'Authorization']);

// reserved names hotfix
utils.reduceDescriptors(AxiosHeaders.prototype, ({value}, key) => {
  let mapped = key[0].toUpperCase() + key.slice(1); // map `set` => `Set`
  return {
    get: () => value,
    set(headerValue) {
      this[mapped] = headerValue;
    }
  }
});

utils.freezeMethods(AxiosHeaders);

/* harmony default export */ const core_AxiosHeaders = (AxiosHeaders);

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/transformData.js






/**
 * Transform the data for a request or a response
 *
 * @param {Array|Function} fns A single function or Array of functions
 * @param {?Object} response The response object
 *
 * @returns {*} The resulting transformed data
 */
function transformData(fns, response) {
  const config = this || lib_defaults;
  const context = response || config;
  const headers = core_AxiosHeaders.from(context.headers);
  let data = context.data;

  utils.forEach(fns, function transform(fn) {
    data = fn.call(config, data, headers.normalize(), response ? response.status : undefined);
  });

  headers.normalize();

  return data;
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/cancel/isCancel.js


function isCancel(value) {
  return !!(value && value.__CANCEL__);
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/cancel/CanceledError.js





/**
 * A `CanceledError` is an object that is thrown when an operation is canceled.
 *
 * @param {string=} message The message.
 * @param {Object=} config The config.
 * @param {Object=} request The request.
 *
 * @returns {CanceledError} The created error.
 */
function CanceledError(message, config, request) {
  // eslint-disable-next-line no-eq-null,eqeqeq
  core_AxiosError.call(this, message == null ? 'canceled' : message, core_AxiosError.ERR_CANCELED, config, request);
  this.name = 'CanceledError';
}

utils.inherits(CanceledError, core_AxiosError, {
  __CANCEL__: true
});

/* harmony default export */ const cancel_CanceledError = (CanceledError);

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/settle.js




/**
 * Resolve or reject a Promise based on response status.
 *
 * @param {Function} resolve A function that resolves the promise.
 * @param {Function} reject A function that rejects the promise.
 * @param {object} response The response.
 *
 * @returns {object} The response.
 */
function settle(resolve, reject, response) {
  const validateStatus = response.config.validateStatus;
  if (!response.status || !validateStatus || validateStatus(response.status)) {
    resolve(response);
  } else {
    reject(new core_AxiosError(
      'Request failed with status code ' + response.status,
      [core_AxiosError.ERR_BAD_REQUEST, core_AxiosError.ERR_BAD_RESPONSE][Math.floor(response.status / 100) - 4],
      response.config,
      response.request,
      response
    ));
  }
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/cookies.js



/* harmony default export */ const cookies = (platform.hasStandardBrowserEnv ?

  // Standard browser envs support document.cookie
  {
    write(name, value, expires, path, domain, secure) {
      const cookie = [name + '=' + encodeURIComponent(value)];

      utils.isNumber(expires) && cookie.push('expires=' + new Date(expires).toGMTString());

      utils.isString(path) && cookie.push('path=' + path);

      utils.isString(domain) && cookie.push('domain=' + domain);

      secure === true && cookie.push('secure');

      document.cookie = cookie.join('; ');
    },

    read(name) {
      const match = document.cookie.match(new RegExp('(^|;\\s*)(' + name + ')=([^;]*)'));
      return (match ? decodeURIComponent(match[3]) : null);
    },

    remove(name) {
      this.write(name, '', Date.now() - 86400000);
    }
  }

  :

  // Non-standard browser env (web workers, react-native) lack needed support.
  {
    write() {},
    read() {
      return null;
    },
    remove() {}
  });


;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/isAbsoluteURL.js


/**
 * Determines whether the specified URL is absolute
 *
 * @param {string} url The URL to test
 *
 * @returns {boolean} True if the specified URL is absolute, otherwise false
 */
function isAbsoluteURL(url) {
  // A URL is considered absolute if it begins with "<scheme>://" or "//" (protocol-relative URL).
  // RFC 3986 defines scheme name as a sequence of characters beginning with a letter and followed
  // by any combination of letters, digits, plus, period, or hyphen.
  return /^([a-z][a-z\d+\-.]*:)?\/\//i.test(url);
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/combineURLs.js


/**
 * Creates a new URL by combining the specified URLs
 *
 * @param {string} baseURL The base URL
 * @param {string} relativeURL The relative URL
 *
 * @returns {string} The combined URL
 */
function combineURLs(baseURL, relativeURL) {
  return relativeURL
    ? baseURL.replace(/\/+$/, '') + '/' + relativeURL.replace(/^\/+/, '')
    : baseURL;
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/buildFullPath.js





/**
 * Creates a new URL by combining the baseURL with the requestedURL,
 * only when the requestedURL is not already an absolute URL.
 * If the requestURL is absolute, this function returns the requestedURL untouched.
 *
 * @param {string} baseURL The base URL
 * @param {string} requestedURL Absolute or relative URL to combine
 *
 * @returns {string} The combined full path
 */
function buildFullPath(baseURL, requestedURL) {
  if (baseURL && !isAbsoluteURL(requestedURL)) {
    return combineURLs(baseURL, requestedURL);
  }
  return requestedURL;
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/isURLSameOrigin.js





/* harmony default export */ const isURLSameOrigin = (platform.hasStandardBrowserEnv ?

// Standard browser envs have full support of the APIs needed to test
// whether the request URL is of the same origin as current location.
  (function standardBrowserEnv() {
    const msie = /(msie|trident)/i.test(navigator.userAgent);
    const urlParsingNode = document.createElement('a');
    let originURL;

    /**
    * Parse a URL to discover its components
    *
    * @param {String} url The URL to be parsed
    * @returns {Object}
    */
    function resolveURL(url) {
      let href = url;

      if (msie) {
        // IE needs attribute set twice to normalize properties
        urlParsingNode.setAttribute('href', href);
        href = urlParsingNode.href;
      }

      urlParsingNode.setAttribute('href', href);

      // urlParsingNode provides the UrlUtils interface - http://url.spec.whatwg.org/#urlutils
      return {
        href: urlParsingNode.href,
        protocol: urlParsingNode.protocol ? urlParsingNode.protocol.replace(/:$/, '') : '',
        host: urlParsingNode.host,
        search: urlParsingNode.search ? urlParsingNode.search.replace(/^\?/, '') : '',
        hash: urlParsingNode.hash ? urlParsingNode.hash.replace(/^#/, '') : '',
        hostname: urlParsingNode.hostname,
        port: urlParsingNode.port,
        pathname: (urlParsingNode.pathname.charAt(0) === '/') ?
          urlParsingNode.pathname :
          '/' + urlParsingNode.pathname
      };
    }

    originURL = resolveURL(window.location.href);

    /**
    * Determine if a URL shares the same origin as the current location
    *
    * @param {String} requestURL The URL to test
    * @returns {boolean} True if URL shares the same origin, otherwise false
    */
    return function isURLSameOrigin(requestURL) {
      const parsed = (utils.isString(requestURL)) ? resolveURL(requestURL) : requestURL;
      return (parsed.protocol === originURL.protocol &&
          parsed.host === originURL.host);
    };
  })() :

  // Non standard browser envs (web workers, react-native) lack needed support.
  (function nonStandardBrowserEnv() {
    return function isURLSameOrigin() {
      return true;
    };
  })());

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/parseProtocol.js


function parseProtocol(url) {
  const match = /^([-+\w]{1,25})(:?\/\/|:)/.exec(url);
  return match && match[1] || '';
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/speedometer.js


/**
 * Calculate data maxRate
 * @param {Number} [samplesCount= 10]
 * @param {Number} [min= 1000]
 * @returns {Function}
 */
function speedometer(samplesCount, min) {
  samplesCount = samplesCount || 10;
  const bytes = new Array(samplesCount);
  const timestamps = new Array(samplesCount);
  let head = 0;
  let tail = 0;
  let firstSampleTS;

  min = min !== undefined ? min : 1000;

  return function push(chunkLength) {
    const now = Date.now();

    const startedAt = timestamps[tail];

    if (!firstSampleTS) {
      firstSampleTS = now;
    }

    bytes[head] = chunkLength;
    timestamps[head] = now;

    let i = tail;
    let bytesCount = 0;

    while (i !== head) {
      bytesCount += bytes[i++];
      i = i % samplesCount;
    }

    head = (head + 1) % samplesCount;

    if (head === tail) {
      tail = (tail + 1) % samplesCount;
    }

    if (now - firstSampleTS < min) {
      return;
    }

    const passed = startedAt && now - startedAt;

    return passed ? Math.round(bytesCount * 1000 / passed) : undefined;
  };
}

/* harmony default export */ const helpers_speedometer = (speedometer);

;// CONCATENATED MODULE: ./node_modules/axios/lib/adapters/xhr.js
















function progressEventReducer(listener, isDownloadStream) {
  let bytesNotified = 0;
  const _speedometer = helpers_speedometer(50, 250);

  return e => {
    const loaded = e.loaded;
    const total = e.lengthComputable ? e.total : undefined;
    const progressBytes = loaded - bytesNotified;
    const rate = _speedometer(progressBytes);
    const inRange = loaded <= total;

    bytesNotified = loaded;

    const data = {
      loaded,
      total,
      progress: total ? (loaded / total) : undefined,
      bytes: progressBytes,
      rate: rate ? rate : undefined,
      estimated: rate && total && inRange ? (total - loaded) / rate : undefined,
      event: e
    };

    data[isDownloadStream ? 'download' : 'upload'] = true;

    listener(data);
  };
}

const isXHRAdapterSupported = typeof XMLHttpRequest !== 'undefined';

/* harmony default export */ const xhr = (isXHRAdapterSupported && function (config) {
  return new Promise(function dispatchXhrRequest(resolve, reject) {
    let requestData = config.data;
    const requestHeaders = core_AxiosHeaders.from(config.headers).normalize();
    let {responseType, withXSRFToken} = config;
    let onCanceled;
    function done() {
      if (config.cancelToken) {
        config.cancelToken.unsubscribe(onCanceled);
      }

      if (config.signal) {
        config.signal.removeEventListener('abort', onCanceled);
      }
    }

    let contentType;

    if (utils.isFormData(requestData)) {
      if (platform.hasStandardBrowserEnv || platform.hasStandardBrowserWebWorkerEnv) {
        requestHeaders.setContentType(false); // Let the browser set it
      } else if ((contentType = requestHeaders.getContentType()) !== false) {
        // fix semicolon duplication issue for ReactNative FormData implementation
        const [type, ...tokens] = contentType ? contentType.split(';').map(token => token.trim()).filter(Boolean) : [];
        requestHeaders.setContentType([type || 'multipart/form-data', ...tokens].join('; '));
      }
    }

    let request = new XMLHttpRequest();

    // HTTP basic authentication
    if (config.auth) {
      const username = config.auth.username || '';
      const password = config.auth.password ? unescape(encodeURIComponent(config.auth.password)) : '';
      requestHeaders.set('Authorization', 'Basic ' + btoa(username + ':' + password));
    }

    const fullPath = buildFullPath(config.baseURL, config.url);

    request.open(config.method.toUpperCase(), buildURL(fullPath, config.params, config.paramsSerializer), true);

    // Set the request timeout in MS
    request.timeout = config.timeout;

    function onloadend() {
      if (!request) {
        return;
      }
      // Prepare the response
      const responseHeaders = core_AxiosHeaders.from(
        'getAllResponseHeaders' in request && request.getAllResponseHeaders()
      );
      const responseData = !responseType || responseType === 'text' || responseType === 'json' ?
        request.responseText : request.response;
      const response = {
        data: responseData,
        status: request.status,
        statusText: request.statusText,
        headers: responseHeaders,
        config,
        request
      };

      settle(function _resolve(value) {
        resolve(value);
        done();
      }, function _reject(err) {
        reject(err);
        done();
      }, response);

      // Clean up request
      request = null;
    }

    if ('onloadend' in request) {
      // Use onloadend if available
      request.onloadend = onloadend;
    } else {
      // Listen for ready state to emulate onloadend
      request.onreadystatechange = function handleLoad() {
        if (!request || request.readyState !== 4) {
          return;
        }

        // The request errored out and we didn't get a response, this will be
        // handled by onerror instead
        // With one exception: request that using file: protocol, most browsers
        // will return status as 0 even though it's a successful request
        if (request.status === 0 && !(request.responseURL && request.responseURL.indexOf('file:') === 0)) {
          return;
        }
        // readystate handler is calling before onerror or ontimeout handlers,
        // so we should call onloadend on the next 'tick'
        setTimeout(onloadend);
      };
    }

    // Handle browser request cancellation (as opposed to a manual cancellation)
    request.onabort = function handleAbort() {
      if (!request) {
        return;
      }

      reject(new core_AxiosError('Request aborted', core_AxiosError.ECONNABORTED, config, request));

      // Clean up request
      request = null;
    };

    // Handle low level network errors
    request.onerror = function handleError() {
      // Real errors are hidden from us by the browser
      // onerror should only fire if it's a network error
      reject(new core_AxiosError('Network Error', core_AxiosError.ERR_NETWORK, config, request));

      // Clean up request
      request = null;
    };

    // Handle timeout
    request.ontimeout = function handleTimeout() {
      let timeoutErrorMessage = config.timeout ? 'timeout of ' + config.timeout + 'ms exceeded' : 'timeout exceeded';
      const transitional = config.transitional || defaults_transitional;
      if (config.timeoutErrorMessage) {
        timeoutErrorMessage = config.timeoutErrorMessage;
      }
      reject(new core_AxiosError(
        timeoutErrorMessage,
        transitional.clarifyTimeoutError ? core_AxiosError.ETIMEDOUT : core_AxiosError.ECONNABORTED,
        config,
        request));

      // Clean up request
      request = null;
    };

    // Add xsrf header
    // This is only done if running in a standard browser environment.
    // Specifically not if we're in a web worker, or react-native.
    if(platform.hasStandardBrowserEnv) {
      withXSRFToken && utils.isFunction(withXSRFToken) && (withXSRFToken = withXSRFToken(config));

      if (withXSRFToken || (withXSRFToken !== false && isURLSameOrigin(fullPath))) {
        // Add xsrf header
        const xsrfValue = config.xsrfHeaderName && config.xsrfCookieName && cookies.read(config.xsrfCookieName);

        if (xsrfValue) {
          requestHeaders.set(config.xsrfHeaderName, xsrfValue);
        }
      }
    }

    // Remove Content-Type if data is undefined
    requestData === undefined && requestHeaders.setContentType(null);

    // Add headers to the request
    if ('setRequestHeader' in request) {
      utils.forEach(requestHeaders.toJSON(), function setRequestHeader(val, key) {
        request.setRequestHeader(key, val);
      });
    }

    // Add withCredentials to request if needed
    if (!utils.isUndefined(config.withCredentials)) {
      request.withCredentials = !!config.withCredentials;
    }

    // Add responseType to request if needed
    if (responseType && responseType !== 'json') {
      request.responseType = config.responseType;
    }

    // Handle progress if needed
    if (typeof config.onDownloadProgress === 'function') {
      request.addEventListener('progress', progressEventReducer(config.onDownloadProgress, true));
    }

    // Not all browsers support upload events
    if (typeof config.onUploadProgress === 'function' && request.upload) {
      request.upload.addEventListener('progress', progressEventReducer(config.onUploadProgress));
    }

    if (config.cancelToken || config.signal) {
      // Handle cancellation
      // eslint-disable-next-line func-names
      onCanceled = cancel => {
        if (!request) {
          return;
        }
        reject(!cancel || cancel.type ? new cancel_CanceledError(null, config, request) : cancel);
        request.abort();
        request = null;
      };

      config.cancelToken && config.cancelToken.subscribe(onCanceled);
      if (config.signal) {
        config.signal.aborted ? onCanceled() : config.signal.addEventListener('abort', onCanceled);
      }
    }

    const protocol = parseProtocol(fullPath);

    if (protocol && platform.protocols.indexOf(protocol) === -1) {
      reject(new core_AxiosError('Unsupported protocol ' + protocol + ':', core_AxiosError.ERR_BAD_REQUEST, config));
      return;
    }


    // Send the request
    request.send(requestData || null);
  });
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/adapters/adapters.js





const knownAdapters = {
  http: helpers_null,
  xhr: xhr
}

utils.forEach(knownAdapters, (fn, value) => {
  if (fn) {
    try {
      Object.defineProperty(fn, 'name', {value});
    } catch (e) {
      // eslint-disable-next-line no-empty
    }
    Object.defineProperty(fn, 'adapterName', {value});
  }
});

const renderReason = (reason) => `- ${reason}`;

const isResolvedHandle = (adapter) => utils.isFunction(adapter) || adapter === null || adapter === false;

/* harmony default export */ const adapters = ({
  getAdapter: (adapters) => {
    adapters = utils.isArray(adapters) ? adapters : [adapters];

    const {length} = adapters;
    let nameOrAdapter;
    let adapter;

    const rejectedReasons = {};

    for (let i = 0; i < length; i++) {
      nameOrAdapter = adapters[i];
      let id;

      adapter = nameOrAdapter;

      if (!isResolvedHandle(nameOrAdapter)) {
        adapter = knownAdapters[(id = String(nameOrAdapter)).toLowerCase()];

        if (adapter === undefined) {
          throw new core_AxiosError(`Unknown adapter '${id}'`);
        }
      }

      if (adapter) {
        break;
      }

      rejectedReasons[id || '#' + i] = adapter;
    }

    if (!adapter) {

      const reasons = Object.entries(rejectedReasons)
        .map(([id, state]) => `adapter ${id} ` +
          (state === false ? 'is not supported by the environment' : 'is not available in the build')
        );

      let s = length ?
        (reasons.length > 1 ? 'since :\n' + reasons.map(renderReason).join('\n') : ' ' + renderReason(reasons[0])) :
        'as no adapter specified';

      throw new core_AxiosError(
        `There is no suitable adapter to dispatch the request ` + s,
        'ERR_NOT_SUPPORT'
      );
    }

    return adapter;
  },
  adapters: knownAdapters
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/dispatchRequest.js









/**
 * Throws a `CanceledError` if cancellation has been requested.
 *
 * @param {Object} config The config that is to be used for the request
 *
 * @returns {void}
 */
function throwIfCancellationRequested(config) {
  if (config.cancelToken) {
    config.cancelToken.throwIfRequested();
  }

  if (config.signal && config.signal.aborted) {
    throw new cancel_CanceledError(null, config);
  }
}

/**
 * Dispatch a request to the server using the configured adapter.
 *
 * @param {object} config The config that is to be used for the request
 *
 * @returns {Promise} The Promise to be fulfilled
 */
function dispatchRequest(config) {
  throwIfCancellationRequested(config);

  config.headers = core_AxiosHeaders.from(config.headers);

  // Transform request data
  config.data = transformData.call(
    config,
    config.transformRequest
  );

  if (['post', 'put', 'patch'].indexOf(config.method) !== -1) {
    config.headers.setContentType('application/x-www-form-urlencoded', false);
  }

  const adapter = adapters.getAdapter(config.adapter || lib_defaults.adapter);

  return adapter(config).then(function onAdapterResolution(response) {
    throwIfCancellationRequested(config);

    // Transform response data
    response.data = transformData.call(
      config,
      config.transformResponse,
      response
    );

    response.headers = core_AxiosHeaders.from(response.headers);

    return response;
  }, function onAdapterRejection(reason) {
    if (!isCancel(reason)) {
      throwIfCancellationRequested(config);

      // Transform response data
      if (reason && reason.response) {
        reason.response.data = transformData.call(
          config,
          config.transformResponse,
          reason.response
        );
        reason.response.headers = core_AxiosHeaders.from(reason.response.headers);
      }
    }

    return Promise.reject(reason);
  });
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/mergeConfig.js





const headersToObject = (thing) => thing instanceof core_AxiosHeaders ? thing.toJSON() : thing;

/**
 * Config-specific merge-function which creates a new config-object
 * by merging two configuration objects together.
 *
 * @param {Object} config1
 * @param {Object} config2
 *
 * @returns {Object} New object resulting from merging config2 to config1
 */
function mergeConfig(config1, config2) {
  // eslint-disable-next-line no-param-reassign
  config2 = config2 || {};
  const config = {};

  function getMergedValue(target, source, caseless) {
    if (utils.isPlainObject(target) && utils.isPlainObject(source)) {
      return utils.merge.call({caseless}, target, source);
    } else if (utils.isPlainObject(source)) {
      return utils.merge({}, source);
    } else if (utils.isArray(source)) {
      return source.slice();
    }
    return source;
  }

  // eslint-disable-next-line consistent-return
  function mergeDeepProperties(a, b, caseless) {
    if (!utils.isUndefined(b)) {
      return getMergedValue(a, b, caseless);
    } else if (!utils.isUndefined(a)) {
      return getMergedValue(undefined, a, caseless);
    }
  }

  // eslint-disable-next-line consistent-return
  function valueFromConfig2(a, b) {
    if (!utils.isUndefined(b)) {
      return getMergedValue(undefined, b);
    }
  }

  // eslint-disable-next-line consistent-return
  function defaultToConfig2(a, b) {
    if (!utils.isUndefined(b)) {
      return getMergedValue(undefined, b);
    } else if (!utils.isUndefined(a)) {
      return getMergedValue(undefined, a);
    }
  }

  // eslint-disable-next-line consistent-return
  function mergeDirectKeys(a, b, prop) {
    if (prop in config2) {
      return getMergedValue(a, b);
    } else if (prop in config1) {
      return getMergedValue(undefined, a);
    }
  }

  const mergeMap = {
    url: valueFromConfig2,
    method: valueFromConfig2,
    data: valueFromConfig2,
    baseURL: defaultToConfig2,
    transformRequest: defaultToConfig2,
    transformResponse: defaultToConfig2,
    paramsSerializer: defaultToConfig2,
    timeout: defaultToConfig2,
    timeoutMessage: defaultToConfig2,
    withCredentials: defaultToConfig2,
    withXSRFToken: defaultToConfig2,
    adapter: defaultToConfig2,
    responseType: defaultToConfig2,
    xsrfCookieName: defaultToConfig2,
    xsrfHeaderName: defaultToConfig2,
    onUploadProgress: defaultToConfig2,
    onDownloadProgress: defaultToConfig2,
    decompress: defaultToConfig2,
    maxContentLength: defaultToConfig2,
    maxBodyLength: defaultToConfig2,
    beforeRedirect: defaultToConfig2,
    transport: defaultToConfig2,
    httpAgent: defaultToConfig2,
    httpsAgent: defaultToConfig2,
    cancelToken: defaultToConfig2,
    socketPath: defaultToConfig2,
    responseEncoding: defaultToConfig2,
    validateStatus: mergeDirectKeys,
    headers: (a, b) => mergeDeepProperties(headersToObject(a), headersToObject(b), true)
  };

  utils.forEach(Object.keys(Object.assign({}, config1, config2)), function computeConfigValue(prop) {
    const merge = mergeMap[prop] || mergeDeepProperties;
    const configValue = merge(config1[prop], config2[prop], prop);
    (utils.isUndefined(configValue) && merge !== mergeDirectKeys) || (config[prop] = configValue);
  });

  return config;
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/env/data.js
const VERSION = "1.6.2";
;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/validator.js





const validators = {};

// eslint-disable-next-line func-names
['object', 'boolean', 'number', 'function', 'string', 'symbol'].forEach((type, i) => {
  validators[type] = function validator(thing) {
    return typeof thing === type || 'a' + (i < 1 ? 'n ' : ' ') + type;
  };
});

const deprecatedWarnings = {};

/**
 * Transitional option validator
 *
 * @param {function|boolean?} validator - set to false if the transitional option has been removed
 * @param {string?} version - deprecated version / removed since version
 * @param {string?} message - some message with additional info
 *
 * @returns {function}
 */
validators.transitional = function transitional(validator, version, message) {
  function formatMessage(opt, desc) {
    return '[Axios v' + VERSION + '] Transitional option \'' + opt + '\'' + desc + (message ? '. ' + message : '');
  }

  // eslint-disable-next-line func-names
  return (value, opt, opts) => {
    if (validator === false) {
      throw new core_AxiosError(
        formatMessage(opt, ' has been removed' + (version ? ' in ' + version : '')),
        core_AxiosError.ERR_DEPRECATED
      );
    }

    if (version && !deprecatedWarnings[opt]) {
      deprecatedWarnings[opt] = true;
      // eslint-disable-next-line no-console
      console.warn(
        formatMessage(
          opt,
          ' has been deprecated since v' + version + ' and will be removed in the near future'
        )
      );
    }

    return validator ? validator(value, opt, opts) : true;
  };
};

/**
 * Assert object's properties type
 *
 * @param {object} options
 * @param {object} schema
 * @param {boolean?} allowUnknown
 *
 * @returns {object}
 */

function assertOptions(options, schema, allowUnknown) {
  if (typeof options !== 'object') {
    throw new core_AxiosError('options must be an object', core_AxiosError.ERR_BAD_OPTION_VALUE);
  }
  const keys = Object.keys(options);
  let i = keys.length;
  while (i-- > 0) {
    const opt = keys[i];
    const validator = schema[opt];
    if (validator) {
      const value = options[opt];
      const result = value === undefined || validator(value, opt, options);
      if (result !== true) {
        throw new core_AxiosError('option ' + opt + ' must be ' + result, core_AxiosError.ERR_BAD_OPTION_VALUE);
      }
      continue;
    }
    if (allowUnknown !== true) {
      throw new core_AxiosError('Unknown option ' + opt, core_AxiosError.ERR_BAD_OPTION);
    }
  }
}

/* harmony default export */ const validator = ({
  assertOptions,
  validators
});

;// CONCATENATED MODULE: ./node_modules/axios/lib/core/Axios.js











const Axios_validators = validator.validators;

/**
 * Create a new instance of Axios
 *
 * @param {Object} instanceConfig The default config for the instance
 *
 * @return {Axios} A new instance of Axios
 */
class Axios {
  constructor(instanceConfig) {
    this.defaults = instanceConfig;
    this.interceptors = {
      request: new core_InterceptorManager(),
      response: new core_InterceptorManager()
    };
  }

  /**
   * Dispatch a request
   *
   * @param {String|Object} configOrUrl The config specific for this request (merged with this.defaults)
   * @param {?Object} config
   *
   * @returns {Promise} The Promise to be fulfilled
   */
  request(configOrUrl, config) {
    /*eslint no-param-reassign:0*/
    // Allow for axios('example/url'[, config]) a la fetch API
    if (typeof configOrUrl === 'string') {
      config = config || {};
      config.url = configOrUrl;
    } else {
      config = configOrUrl || {};
    }

    config = mergeConfig(this.defaults, config);

    const {transitional, paramsSerializer, headers} = config;

    if (transitional !== undefined) {
      validator.assertOptions(transitional, {
        silentJSONParsing: Axios_validators.transitional(Axios_validators.boolean),
        forcedJSONParsing: Axios_validators.transitional(Axios_validators.boolean),
        clarifyTimeoutError: Axios_validators.transitional(Axios_validators.boolean)
      }, false);
    }

    if (paramsSerializer != null) {
      if (utils.isFunction(paramsSerializer)) {
        config.paramsSerializer = {
          serialize: paramsSerializer
        }
      } else {
        validator.assertOptions(paramsSerializer, {
          encode: Axios_validators.function,
          serialize: Axios_validators.function
        }, true);
      }
    }

    // Set config.method
    config.method = (config.method || this.defaults.method || 'get').toLowerCase();

    // Flatten headers
    let contextHeaders = headers && utils.merge(
      headers.common,
      headers[config.method]
    );

    headers && utils.forEach(
      ['delete', 'get', 'head', 'post', 'put', 'patch', 'common'],
      (method) => {
        delete headers[method];
      }
    );

    config.headers = core_AxiosHeaders.concat(contextHeaders, headers);

    // filter out skipped interceptors
    const requestInterceptorChain = [];
    let synchronousRequestInterceptors = true;
    this.interceptors.request.forEach(function unshiftRequestInterceptors(interceptor) {
      if (typeof interceptor.runWhen === 'function' && interceptor.runWhen(config) === false) {
        return;
      }

      synchronousRequestInterceptors = synchronousRequestInterceptors && interceptor.synchronous;

      requestInterceptorChain.unshift(interceptor.fulfilled, interceptor.rejected);
    });

    const responseInterceptorChain = [];
    this.interceptors.response.forEach(function pushResponseInterceptors(interceptor) {
      responseInterceptorChain.push(interceptor.fulfilled, interceptor.rejected);
    });

    let promise;
    let i = 0;
    let len;

    if (!synchronousRequestInterceptors) {
      const chain = [dispatchRequest.bind(this), undefined];
      chain.unshift.apply(chain, requestInterceptorChain);
      chain.push.apply(chain, responseInterceptorChain);
      len = chain.length;

      promise = Promise.resolve(config);

      while (i < len) {
        promise = promise.then(chain[i++], chain[i++]);
      }

      return promise;
    }

    len = requestInterceptorChain.length;

    let newConfig = config;

    i = 0;

    while (i < len) {
      const onFulfilled = requestInterceptorChain[i++];
      const onRejected = requestInterceptorChain[i++];
      try {
        newConfig = onFulfilled(newConfig);
      } catch (error) {
        onRejected.call(this, error);
        break;
      }
    }

    try {
      promise = dispatchRequest.call(this, newConfig);
    } catch (error) {
      return Promise.reject(error);
    }

    i = 0;
    len = responseInterceptorChain.length;

    while (i < len) {
      promise = promise.then(responseInterceptorChain[i++], responseInterceptorChain[i++]);
    }

    return promise;
  }

  getUri(config) {
    config = mergeConfig(this.defaults, config);
    const fullPath = buildFullPath(config.baseURL, config.url);
    return buildURL(fullPath, config.params, config.paramsSerializer);
  }
}

// Provide aliases for supported request methods
utils.forEach(['delete', 'get', 'head', 'options'], function forEachMethodNoData(method) {
  /*eslint func-names:0*/
  Axios.prototype[method] = function(url, config) {
    return this.request(mergeConfig(config || {}, {
      method,
      url,
      data: (config || {}).data
    }));
  };
});

utils.forEach(['post', 'put', 'patch'], function forEachMethodWithData(method) {
  /*eslint func-names:0*/

  function generateHTTPMethod(isForm) {
    return function httpMethod(url, data, config) {
      return this.request(mergeConfig(config || {}, {
        method,
        headers: isForm ? {
          'Content-Type': 'multipart/form-data'
        } : {},
        url,
        data
      }));
    };
  }

  Axios.prototype[method] = generateHTTPMethod();

  Axios.prototype[method + 'Form'] = generateHTTPMethod(true);
});

/* harmony default export */ const core_Axios = (Axios);

;// CONCATENATED MODULE: ./node_modules/axios/lib/cancel/CancelToken.js




/**
 * A `CancelToken` is an object that can be used to request cancellation of an operation.
 *
 * @param {Function} executor The executor function.
 *
 * @returns {CancelToken}
 */
class CancelToken {
  constructor(executor) {
    if (typeof executor !== 'function') {
      throw new TypeError('executor must be a function.');
    }

    let resolvePromise;

    this.promise = new Promise(function promiseExecutor(resolve) {
      resolvePromise = resolve;
    });

    const token = this;

    // eslint-disable-next-line func-names
    this.promise.then(cancel => {
      if (!token._listeners) return;

      let i = token._listeners.length;

      while (i-- > 0) {
        token._listeners[i](cancel);
      }
      token._listeners = null;
    });

    // eslint-disable-next-line func-names
    this.promise.then = onfulfilled => {
      let _resolve;
      // eslint-disable-next-line func-names
      const promise = new Promise(resolve => {
        token.subscribe(resolve);
        _resolve = resolve;
      }).then(onfulfilled);

      promise.cancel = function reject() {
        token.unsubscribe(_resolve);
      };

      return promise;
    };

    executor(function cancel(message, config, request) {
      if (token.reason) {
        // Cancellation has already been requested
        return;
      }

      token.reason = new cancel_CanceledError(message, config, request);
      resolvePromise(token.reason);
    });
  }

  /**
   * Throws a `CanceledError` if cancellation has been requested.
   */
  throwIfRequested() {
    if (this.reason) {
      throw this.reason;
    }
  }

  /**
   * Subscribe to the cancel signal
   */

  subscribe(listener) {
    if (this.reason) {
      listener(this.reason);
      return;
    }

    if (this._listeners) {
      this._listeners.push(listener);
    } else {
      this._listeners = [listener];
    }
  }

  /**
   * Unsubscribe from the cancel signal
   */

  unsubscribe(listener) {
    if (!this._listeners) {
      return;
    }
    const index = this._listeners.indexOf(listener);
    if (index !== -1) {
      this._listeners.splice(index, 1);
    }
  }

  /**
   * Returns an object that contains a new `CancelToken` and a function that, when called,
   * cancels the `CancelToken`.
   */
  static source() {
    let cancel;
    const token = new CancelToken(function executor(c) {
      cancel = c;
    });
    return {
      token,
      cancel
    };
  }
}

/* harmony default export */ const cancel_CancelToken = (CancelToken);

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/spread.js


/**
 * Syntactic sugar for invoking a function and expanding an array for arguments.
 *
 * Common use case would be to use `Function.prototype.apply`.
 *
 *  ```js
 *  function f(x, y, z) {}
 *  var args = [1, 2, 3];
 *  f.apply(null, args);
 *  ```
 *
 * With `spread` this example can be re-written.
 *
 *  ```js
 *  spread(function(x, y, z) {})([1, 2, 3]);
 *  ```
 *
 * @param {Function} callback
 *
 * @returns {Function}
 */
function spread(callback) {
  return function wrap(arr) {
    return callback.apply(null, arr);
  };
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/isAxiosError.js




/**
 * Determines whether the payload is an error thrown by Axios
 *
 * @param {*} payload The value to test
 *
 * @returns {boolean} True if the payload is an error thrown by Axios, otherwise false
 */
function isAxiosError(payload) {
  return utils.isObject(payload) && (payload.isAxiosError === true);
}

;// CONCATENATED MODULE: ./node_modules/axios/lib/helpers/HttpStatusCode.js
const HttpStatusCode = {
  Continue: 100,
  SwitchingProtocols: 101,
  Processing: 102,
  EarlyHints: 103,
  Ok: 200,
  Created: 201,
  Accepted: 202,
  NonAuthoritativeInformation: 203,
  NoContent: 204,
  ResetContent: 205,
  PartialContent: 206,
  MultiStatus: 207,
  AlreadyReported: 208,
  ImUsed: 226,
  MultipleChoices: 300,
  MovedPermanently: 301,
  Found: 302,
  SeeOther: 303,
  NotModified: 304,
  UseProxy: 305,
  Unused: 306,
  TemporaryRedirect: 307,
  PermanentRedirect: 308,
  BadRequest: 400,
  Unauthorized: 401,
  PaymentRequired: 402,
  Forbidden: 403,
  NotFound: 404,
  MethodNotAllowed: 405,
  NotAcceptable: 406,
  ProxyAuthenticationRequired: 407,
  RequestTimeout: 408,
  Conflict: 409,
  Gone: 410,
  LengthRequired: 411,
  PreconditionFailed: 412,
  PayloadTooLarge: 413,
  UriTooLong: 414,
  UnsupportedMediaType: 415,
  RangeNotSatisfiable: 416,
  ExpectationFailed: 417,
  ImATeapot: 418,
  MisdirectedRequest: 421,
  UnprocessableEntity: 422,
  Locked: 423,
  FailedDependency: 424,
  TooEarly: 425,
  UpgradeRequired: 426,
  PreconditionRequired: 428,
  TooManyRequests: 429,
  RequestHeaderFieldsTooLarge: 431,
  UnavailableForLegalReasons: 451,
  InternalServerError: 500,
  NotImplemented: 501,
  BadGateway: 502,
  ServiceUnavailable: 503,
  GatewayTimeout: 504,
  HttpVersionNotSupported: 505,
  VariantAlsoNegotiates: 506,
  InsufficientStorage: 507,
  LoopDetected: 508,
  NotExtended: 510,
  NetworkAuthenticationRequired: 511,
};

Object.entries(HttpStatusCode).forEach(([key, value]) => {
  HttpStatusCode[value] = key;
});

/* harmony default export */ const helpers_HttpStatusCode = (HttpStatusCode);

;// CONCATENATED MODULE: ./node_modules/axios/lib/axios.js




















/**
 * Create an instance of Axios
 *
 * @param {Object} defaultConfig The default config for the instance
 *
 * @returns {Axios} A new instance of Axios
 */
function createInstance(defaultConfig) {
  const context = new core_Axios(defaultConfig);
  const instance = bind(core_Axios.prototype.request, context);

  // Copy axios.prototype to instance
  utils.extend(instance, core_Axios.prototype, context, {allOwnKeys: true});

  // Copy context to instance
  utils.extend(instance, context, null, {allOwnKeys: true});

  // Factory for creating new instances
  instance.create = function create(instanceConfig) {
    return createInstance(mergeConfig(defaultConfig, instanceConfig));
  };

  return instance;
}

// Create the default instance to be exported
const axios = createInstance(lib_defaults);

// Expose Axios class to allow class inheritance
axios.Axios = core_Axios;

// Expose Cancel & CancelToken
axios.CanceledError = cancel_CanceledError;
axios.CancelToken = cancel_CancelToken;
axios.isCancel = isCancel;
axios.VERSION = VERSION;
axios.toFormData = helpers_toFormData;

// Expose AxiosError class
axios.AxiosError = core_AxiosError;

// alias for CanceledError for backward compatibility
axios.Cancel = axios.CanceledError;

// Expose all/spread
axios.all = function all(promises) {
  return Promise.all(promises);
};

axios.spread = spread;

// Expose isAxiosError
axios.isAxiosError = isAxiosError;

// Expose mergeConfig
axios.mergeConfig = mergeConfig;

axios.AxiosHeaders = core_AxiosHeaders;

axios.formToJSON = thing => helpers_formDataToJSON(utils.isHTMLForm(thing) ? new FormData(thing) : thing);

axios.getAdapter = adapters.getAdapter;

axios.HttpStatusCode = helpers_HttpStatusCode;

axios.default = axios;

// this module should only have a default export
/* harmony default export */ const lib_axios = (axios);

;// CONCATENATED MODULE: ./browser/window/WindowUtil.js


const log = console; 
const __serverApi = '';
const __isLocal = window.location.host == 'localhost';
top.__isLocal = __isLocal;
class WindowUtil{
    #loginRemeber;
    constructor(){

    }
    async isLogin(callBack = () => {}){
        if(this.#loginRemeber){
            return Promise.resolve(callBack(this.#loginRemeber));
        }
        return lib_axios.get(__serverApi + '/api/account/search/is-login', {
            headers:{
                'Content-Type': 'application/json'
            }
        })
        .catch(err=>{
            return err.response
        })
        .then(response => {
            if( ! this.responseIsOk(response)){
                return callBack({
                    isLogin: false,
                    status: response?.status,
                    statusText: response?.statusText
                });
            }else{
                if(response.data.code == 0){
                    response.isLogin = true;	
                }else if(response.data.code == 100 || response.data.code == 105 || response.data.code == 106 || response.data.code == 107){
                    response.isLogin = false;
                }else{
                    response.isLogin = false;
                }

                if(! response.isLogin){
                    lib_axios.defaults.headers.common['Authorization'] = '';
                }else{
                    this.#loginRemeber = response;
                    setTimeout(()=>{
                        this.#loginRemeber = undefined;
                    }, 1000 * 60)
                }
                return callBack(response);
            }
        })
    }

    responseIsOk({status = undefined} = {}){
        if( ! status){
            return false;
        }
        return (status == '200' || status == '201') ;
    }

    responseCheck(response){
		let status = response.status;
		let {code, data} = response.data;
        if(status == '200' || status == '201'){
			return response
		}else{
            log.error(response);
			throw new Error(response.message);
		}
	}

}

const windowUtil = new WindowUtil();
;// CONCATENATED MODULE: ./browser/controller/AccountController.js
 

const AccountController_log = console;

class AccountController {
	constructor() {

	}

	loginProcessing(param){
		param = Object.entries(param).reduce((total, [k,v]) => {
			if(v != undefined && v != ''){
				total[k] = v;
			}
			return total;
		},{});
		return lib_axios.post(__serverApi + '/login-processing', JSON.stringify(param), {
			headers:{
				'Content-Type': 'application/json'
			}
		})
		.then(response=>{
			if(windowUtil.responseIsOk(response) && top.constructor.name == 'Window'){
				return response.data;
			}
			return response;	
		}).catch(err=>{
			AccountController_log.error('loginProcessing error : ', err.message);
			lib_axios.defaults.headers.common['Authorization'] = '';
			console.log(err);
			if(err.response){
				return err.response.data;
			}else{
				return err.message
			}
			
		})
	}
	getAccountInfo(){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/account/search/get-account-info`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response=>response.data)
				.catch(err=>{
					AccountController_log.error('IPC getAccountInfo error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		});
	}
	updateSimpleAccountInfo(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/account/update/simple-account-info`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					AccountController_log.error('IPC createPermitWokrspaceInAccount error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			AccountController_log.error('error ::: ', error.message)
			AccountController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
}
const accountController = new AccountController();
;// CONCATENATED MODULE: ./browser/controller/ApiS3Controller.js
 

const ApiS3Controller_log = console;

class ApiS3Controller {
	constructor() {
	}
	generatePutObjectPresignedUrl(param){
		return windowUtil.isLogin( result => {
			param = Object.entries(param).reduce((total, [k,v]) => {
				if(v != undefined && v != ''){
					total[k] = v;
				}
				return total;
			},{});
			if(result.isLogin){
				return lib_axios.post(`${__serverApi}/api/generate-presigned-url/create/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					ApiS3Controller_log.error('IPC sendChatting error', err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			ApiS3Controller_log.error('sendChatting login error ::: ', error.message);
			ApiS3Controller_log.error('sendChatting login error stack ::: ', error.stack);
			return undefined;
		});
	}
	generateGetObjectPresignedUrl(param){
		return windowUtil.isLogin( result => {
			param = Object.entries(param).reduce((total, [k,v]) => {
				if(v != undefined && v != ''){
					total[k] = v;
				}
				return total;
			},{});
			if(result.isLogin){
				return lib_axios.post(`${__serverApi}/api/generate-presigned-url/search/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					ApiS3Controller_log.error('IPC sendChatting error', err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			ApiS3Controller_log.error('sendChatting login error ::: ', error.message);
			ApiS3Controller_log.error('sendChatting login error stack ::: ', error.stack);
			return undefined;
		});
	}
}
const apiS3Controller = new ApiS3Controller();
;// CONCATENATED MODULE: ./browser/controller/ChattingController.js
 

const ChattingController_log = console;

class ChattingController {
	constructor() {

	}

	sendChatting(param){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});
				return lib_axios.post(`${__serverApi}/api/chatting/create/send-chatting`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					ChattingController_log.error('IPC sendChatting error', err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			ChattingController_log.error('sendChatting login error ::: ', error.message);
			ChattingController_log.error('sendChatting login error stack ::: ', error.stack);
			return undefined;
		});
	}	
	deleteChatting(param){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});
				return lib_axios.post(`${__serverApi}/api/chatting/delete/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					ChattingController_log.error('IPC sendChatting error', err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			ChattingController_log.error('sendChatting login error ::: ', error.message);
			ChattingController_log.error('sendChatting login error stack ::: ', error.stack);
			return undefined;
		});
	}
	searchChattingList(param){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let {workspaceId, roomId} = param;
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId' && k != 'roomId')
					.map(([k,v]) => `${k}=${v}`).join('&')
				return lib_axios.get(`${__serverApi}/api/chatting/search/chatting-list/${workspaceId}/${roomId}?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data;
				}).catch(err=>{
					ChattingController_log.error('IPC searchChatting error' , err);
					return err.response.data;
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			ChattingController_log.error('searchChatting login error ::: ', error.message);
			ChattingController_log.error('searchChatting login error stack ::: ', error.stack);
			return undefined;
		})
	}
}
const chattingController = new ChattingController();
;// CONCATENATED MODULE: ./browser/controller/EmoticonController.js
 

const EmoticonController_log = console;

class EmoticonController {
	constructor() {
    }

	createEmotionReaction(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/emoticon/create/reaction`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					EmoticonController_log.error('IPC createEmotion error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			EmoticonController_log.error('error ::: ', error.message)
			EmoticonController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
    deleteEmotion(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/emoticon/delete/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					EmoticonController_log.error('IPC deleteEmotion error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			EmoticonController_log.error('error ::: ', error.message)
			EmoticonController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	getIsReaction(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let {workspaceId} = param;
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId')
					.map(([k,v]) => {
						if(v instanceof Array){
							v = v.map(val=>`${k}=${val}`).join('&')
							return v;
						}
						return `${k}=${v}`
					}).join('&')
				return lib_axios.get(`${__serverApi}/api/emoticon/search/is-reaction?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					EmoticonController_log.error('IPC searchMyJoinedRoomList error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			EmoticonController_log.error('error ::: ', error.message)
			EmoticonController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	
}
const emoticonController = new EmoticonController();
;// CONCATENATED MODULE: ./browser/controller/EventStreamController.js
 

const EventStreamController_log = console;

class EventStreamController {
    #source;
    #isConnectSource = false;
	prevWorkspaceId;
    constructor(){

    }

	initWorkspaceStream(param, EventSource, eventSendObj){
        let {workspaceId} = param;
        console.log(top.EventSource);
        if( ! EventSource) EventSource = top?.EventSource;
		if(this.prevWorkspaceId == workspaceId && (this.source?.readyState == 1 || this.source?.readyState == 0)){
            return;
        }else if(this.prevWorkspaceId != workspaceId && (this.source?.readyState == 1 || this.source?.readyState == 0)){
            this.source.close();
        }
        
        this.prevWorkspaceId = workspaceId;

        this.source = new EventSource(`${__serverApi}/api/event-stream/workspace/${workspaceId}`, {
            headers: {
                'Authorization' : lib_axios.defaults.headers.common['Authorization'],
            },
            withCredentials : ! top.__isLocal
        });

        this.source.onmessage = (event) => {
            let {data, lastEventId, origin, type} = event;
            data = JSON.parse(data);
            EventStreamController_log.debug('event stream data ::: ', data);

            let eventName = data.serverSentStreamType.split('_').map((e, i)=>{
                if(i == 0){
                    return e.toLowerCase(); 
                }
                return e.charAt(0) + e.substring(1).toLowerCase();
            }).join('');

            EventStreamController_log.debug('on message: ', event.data, 'eventName ::', eventName);

            if(this[eventName]){
                this[eventName](eventName, data);
                return ;
            }
            if(eventSendObj.webEventSend){
                eventSendObj.webEventSend(eventName, data);
            }else{
                eventSendObj.send(eventName, data);
            }
        };

        this.source.onerror = (error) => {
            EventStreamController_log.error('on stream err: ', error);
            //log.debug('source ::: ', this.source);
            /*this.#isConnectSource = false;
            windowUtil.isLogin( result => {
                if( ! result.isLogin){
                    axios.defaults.headers.common['Authorization'] = '';
                    this.source.close();
                    this.#send('needLoginRequest', result);
                }
            })*/
            //연결 실패되면 계속 시도하기에 임시 조치로 close
            //this.source.close();
            //stop();
        };
        this.source.onopen = (success) => {
            EventStreamController_log.debug('on success: ', success)
        }
        /*
        * This will listen only for events
        * similar to the following:
        *
        * event: notice
        * data: useful data
        * id: someid
        this.source.addEventListener("notice", (e) => {
            log.debug('event notice', e.data);
        });
        */

        /*
        * Similarly, this will listen for events
        * with the field `event: update`
        this.source.addEventListener("update", (e) => {
            log.debug('event update ::: ',e.data);
        });
        */

        /*
        * The event "message" is a special case, as it
        * will capture events without an event field
        * as well as events that have the specific type
        * `event: message` It will not trigger on any
        * other event type.
        this.source.addEventListener("message", (e) => {
            log.debug('message !!!!! : ', e.data);
        });
        */
        //}
	}

}
const eventStreamController = new EventStreamController();
;// CONCATENATED MODULE: ./browser/controller/NoticeBoardController.js
 

const NoticeBoardController_log = console;
class NoticeBoardController {
	constructor() {

    }

    createNoticeBoardGroup(param = {}){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});
                return lib_axios.post(`${__serverApi}/api/notice-board/create/group`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC createNoticeBoardGroup error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }

    createNoticeBoard(param = {}){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});
                return lib_axios.post(`${__serverApi}/api/notice-board/create/`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC createNoticeBoard error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }

    createNoticeBoardDetail(param = {}){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});
                return lib_axios.post(`${__serverApi}/api/notice-board/create/detail`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC createNoticeBoardDetail error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }
    searchNoticeBoardList(param = {}, EventSource, eventSendObj){
        if( ! EventSource) EventSource = top?.EventSource;
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                let {workspaceId, roomId} = param;
                let queryString = Object.entries(param)
                    .filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId' && k != 'roomId')
                    .map(([k,v]) => `${k}=${v}`).join('&')
                    //console.log('queryString ::: ', queryString);
                //return axios.get(`${__serverApi}/api/notice-board/search/notice-board-list/${workspaceId}/${roomId}?${queryString}`, {
                return new Promise(resolve=>{
                    let source = new EventSource(`${__serverApi}/api/notice-board/search/notice-board-list/${workspaceId}/${roomId}?${queryString}`, {
                        headers: {
                            'Authorization' : lib_axios.defaults.headers.common['Authorization'],
                        },
                        withCredentials : ! top.__isLocal
                    });
                    source.onmessage = (event) => {
                        //console.log('test message :::: ',event);
                        let {data, lastEventId, origin, type} = event;
                        data = JSON.parse(data);
                        if(eventSendObj.webEventSend){
							eventSendObj.webEventSend('noticeBoardAccept', data);
						}else{
							eventSendObj.send('noticeBoardAccept', data);
						}
                    }
                    source.onerror = (event) => {
                        //console.log('searchNoticeBoardList error :::: ',event);
                        source.close();
                        resolve('done');
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
            NoticeBoardController_log.error('error ::: ', error.message)
            NoticeBoardController_log.error('error stack :::', error.stack)
            return undefined;
        });
    }

    searchNoticeBoardDetailList(param = {}, EventSource, eventSendObj){
        if( ! EventSource) EventSource = top?.EventSource;
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                let {workspaceId, roomId, noticeBoardId} = param;
                if(! noticeBoardId){
                    return;
                }
                let queryString = Object.entries(param)
                    .filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId' && k != 'roomId' && k != 'noticeBoardId')
                    .map(([k,v]) => `${k}=${v}`).join('&')
                    //console.log('queryString ::: ', queryString);
                //return axios.get(`${__serverApi}/api/notice-board/search/notice-board-detail-list/${workspaceId}/${roomId}/${noticeBoardId}?${queryString}`, {
                return new Promise(resolve=>{
                    let source = new EventSource(`${__serverApi}/api/notice-board/search/notice-board-detail-list/${workspaceId}/${roomId}/${noticeBoardId}?${queryString}`, {
                        headers: {
                            'Authorization' : lib_axios.defaults.headers.common['Authorization'],
                        },
                        withCredentials : ! top.__isLocal
                    });
                    source.onmessage = (event) => {
                        //console.log('test message :::: ',event);
                        let {data, lastEventId, origin, type} = event;
                        data = JSON.parse(data);
                        if(eventSendObj.webEventSend){
							eventSendObj.webEventSend('noticeBoardDetailAccept', data);
						}else{
							eventSendObj.send('noticeBoardDetailAccept', data);
						}
                    }
                    source.onerror = (event) => {
                        console.log('searchNoticeBoardDetailList error :::: ',event);
                        source.close();
                        resolve('done');
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
            NoticeBoardController_log.error('error ::: ', error.message)
            NoticeBoardController_log.error('error stack :::', error.stack)
            return undefined;
        });
    }

    deleteNoticeBoard(param = {}){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});
                console.log('delete param',param)
                return lib_axios.post(`${__serverApi}/api/notice-board/delete/`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC deleteNoticeBoard error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }
    deleteNoticeBoardGroup(param = {}){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                param = Object.entries(param).reduce((total, [k,v]) => {
                    if(v != undefined && v != ''){
                        total[k] = v;
                    }
                    return total;
                },{});

                return lib_axios.post(`${__serverApi}/api/notice-board/delete/group`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC deleteNoticeBoardGroup error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }
    updateNoticeBoardOrder(param = []){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                console.log('update param',param)
                return lib_axios.post(`${__serverApi}/api/notice-board/update/order`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC updateNoticeBoardOrder error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }
    updateNoticeBoardDetailOrder(param = []){
        return windowUtil.isLogin( result => {
            if(result.isLogin){
                return lib_axios.post(`${__serverApi}/api/notice-board/update/detail-order`, JSON.stringify(param), {
                    headers:{
                        'Content-Type': 'application/json'
                    }
                })
                .then(windowUtil.responseCheck)
                .then(response => response.data)
                .catch(err=>{
                    NoticeBoardController_log.error('IPC updateNoticeBoardDetailOrder error : ', JSON.stringify(err));
                    //axios.defaults.headers.common['Authorization'] = '';
                    if(err.response){
                        return err.response.data;
                    }else{
                        return err.message
                    }
                })
            }else{
                return {'isLogin': false};
            }
        }).catch(error=>{
			NoticeBoardController_log.error('error ::: ', error.message)
			NoticeBoardController_log.error('error stack :::', error.stack)
			return undefined;
		})
    }
}
const noticeBoardController = new NoticeBoardController();

;// CONCATENATED MODULE: ./browser/controller/RoomController.js
 

const RoomController_log = console;
class RoomController {
	constructor() {
    }

	createRoom(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/room/create/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC createRoom error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	createMySelfRoom(param = {}){
		if( ! param.workspaceId || isNaN(parseInt(param.workspaceId))){
			RoomController_log.error(`createMySelfRoom workspaceId is ::: ${param.workspaceId}`);
			return undefined;
		}
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.post(`${__serverApi}/api/room/create/my-self-room/${param.workspaceId}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC createMySelfRoom error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	createRoomInAccount(param = []){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.post(`${__serverApi}/api/room/create/in-account`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.then(data => {
					RoomController_log.debug('createRoomInAccount ::: ',data)
					return data;
				})
				.catch(err=>{
					RoomController_log.error('IPC createRoomInAccount error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	updateRoomInAccoutOrder(param = []){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.post(`${__serverApi}/api/room/update/order`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC updateRoomInAccoutOrder error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	createRoomFavorites(param){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/room/create/favorites`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC createRoomFavorites error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	updateRoomFavorites(param = []){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.post(`${__serverApi}/api/room/update/favorites-order`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC createRoom error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchRoomList(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let {roomId} = param;
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'roomId')
					.map(([k,v]) => `${k}=${v}`).join('&')
				return lib_axios.get(`${__serverApi}/api/room/search/list/${roomId}?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC searchRoom error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchMyJoinedRoomList(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let {workspaceId} = param;
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId')
					.map(([k,v]) => {
						if(v instanceof Array){
							v = v.map(val=>`${k}=${val}`).join('&')
							return v;
						}
						return `${k}=${v}`
					}).join('&')
				return lib_axios.get(`${__serverApi}/api/room/search/my-joined-list/${workspaceId}?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC searchMyJoinedRoomList error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchRoomFavoritesList(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let {workspaceId} = param;
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId')
					.map(([k,v]) => {
						if(v instanceof Array){
							v = v.map(val=>`${k}=${val}`).join('&')
							return v;
						}
						return `${k}=${v}`
					}).join('&')
				return lib_axios.get(`${__serverApi}/api/room/search/favorites-list/${workspaceId}?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC searchMyJoinedRoomList error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchRoomJoinedAccountList(param = {}, EventSource, eventSendObj){

		if( ! param.roomId || isNaN(parseInt(param.roomId))){
			RoomController_log.error(`searchRoomJoinedAccountList roomId is ::: ${param.roomId}`);
			return undefined;
		}
		if( ! EventSource) EventSource = top?.EventSource;
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return new Promise(resolve=>{
					let source = new EventSource(`${__serverApi}/api/room/search/in-account-list/${param.roomId}`, {
						headers: {
							'Authorization' : lib_axios.defaults.headers.common['Authorization'],
						},
						withCredentials : ! top.__isLocal
					});
					source.onmessage = (event) => {
						//console.log('test message :::: ',event);
						let {data, lastEventId, origin, type} = event;
						data = JSON.parse(data);
						if(eventSendObj.webEventSend){
							eventSendObj.webEventSend('roomInAccountAccept', data);
						}else{
							eventSendObj.send('roomInAccountAccept', data);
						}
					}
					source.onerror = (event) => {
						//console.log('searchRoomJoinedAccountList error :::: ',event);
						source.close();
						resolve('done');
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	
	getRoomDetail(param = {}){
		if( ! param.roomId || isNaN(parseInt(param.roomId))){
			RoomController_log.error(`getRoomDetail roomId is ::: ${param.roomId}`);
			return undefined;
		}
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/room/search/detail/${param.roomId}`, {
					headers: {
						'Content-Type' : 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
			}else{
				return {'isLogin': false};
			}
		});
	}
	isRoomFavorites(param = {}){
		if( ! param.roomId || isNaN(parseInt(param.roomId))){
			RoomController_log.error(`isRoomFavorites roomId is ::: ${param.roomId}`);
			return undefined;
		}
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/room/search/is-room-favorites/${param.roomId}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					RoomController_log.error('IPC isRoomFavorites error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			RoomController_log.error('error ::: ', error.message)
			RoomController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
}
const roomController = new RoomController();
;// CONCATENATED MODULE: ./browser/controller/WorkspaceController.js
 

const WorkspaceController_log = console;

class WorkspaceController {
	constructor() {
	}
	searchWorkspaceMyJoined(param = {}){
		return windowUtil.isLogin((result) => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/workspace/search/my-joined-list?page=${param.page}&size=${param.size}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response=>{
					return response.data;
				}).catch(err=>{
					WorkspaceController_log.error('error : ', JSON.stringify(err));
					if(err.response){
						if( ! err.response.data.content){
							err.response.data.content = [];
						}
						return err.response.data;
					}else{

					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchNameSpecificList(param = {}){
		return windowUtil.isLogin((result) => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/workspace/search/name-specific-list?page=${param.page}&size=${param.size}&workspaceName=${param.workspaceName}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response=>{
					return response.data;
				}).catch(err=>{
					WorkspaceController_log.error('error : ', JSON.stringify(err));
					if(err.response){
						if( ! err.response.data.content){
							err.response.data.content = [];
						}
						return err.response.data;
					}else{

					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchWorkspaceInAccount(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId')
					.map(([k,v]) => `${k}=${v}`).join('&')
				return lib_axios.get(`${__serverApi}/api/workspace/search/joined-account-list/${param.workspaceId}?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data
				}).catch(err=>{
					WorkspaceController_log.error('IPC searchWorkspaceInAccount error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	getWorkspaceDetail(param = {}){
		if( ! param.workspaceId || isNaN(parseInt(param.workspaceId))){
			WorkspaceController_log.error(`workspaceId is ::: ${param.workspaceId}`);
			return undefined;
		}
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/workspace/search/detail/${param.workspaceId}`, {
					headers: {
						'Content-Type' : 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data.data;
				})
			}else{
				return {'isLogin': false};
			}
		});
	}
	getWorkspaceInAccountCount(param = {}){
		if( ! param.workspaceId || isNaN(parseInt(param.workspaceId))){
			WorkspaceController_log.error(`workspaceId is ::: ${param.workspaceId}`);
			return undefined;
		}
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				return lib_axios.get(`${__serverApi}/api/workspace/search/count/${param.workspaceId}`, {
					headers: {
						'Content-Type' : 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data.data;
				})
			}else{
				return {'isLogin': false};
			}
		});
	}
	createPermitWokrspaceInAccount(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/workspace/create/permit`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					WorkspaceController_log.error('IPC createPermitWokrspaceInAccount error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	createGiveAdmin(param={}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/workspace/create/give-admin`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					WorkspaceController_log.error('IPC createPermitWokrspaceInAccount error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	searchPermitRequestList(param = {}, EventSource, eventSendObj){
		if( ! param.workspaceId || isNaN(parseInt(param.workspaceId))){
			WorkspaceController_log.error(`searchPermitRequestList workspaceId is ::: ${param.workspaceId}`);
			return undefined;
		}
		if( ! EventSource) EventSource = top?.EventSource;
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				//return axios.get(`${__serverApi}/api/workspace/search/permit-request-list/${param.workspaceId}`, {
				return new Promise(resolve=>{
					let source = new EventSource(`${__serverApi}/api/workspace/search/permit-request-list/${param.workspaceId}`, {
						headers: {
							'Authorization' : lib_axios.defaults.headers.common['Authorization'],
						},
						withCredentials : ! top.__isLocal
					});
					source.onmessage = (event) => {
						//console.log('test message :::: ',event);
						let {data, lastEventId, origin, type} = event;
						data = JSON.parse(data);
						if(eventSendObj.webEventSend){
							eventSendObj.webEventSend('workspacePermitRequestAccept', data);
						}else{
							eventSendObj.send('workspacePermitRequestAccept', data);
						}
					}
					source.onerror = (event) => {
						//console.log('searchPermitRequestList error :::: ',event);
						source.close();
						resolve('done');
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	getIsAdmin(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				let queryString = Object.entries(param)
					.filter(([k,v]) => v != undefined && v != '' && k != 'workspaceId')
					.map(([k,v]) => `${k}=${v}`).join('&')
				return lib_axios.get(`${__serverApi}/api/workspace/search/is-admin/${param.workspaceId}?${queryString}`, {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data
				}).catch(err=>{
					WorkspaceController_log.error('IPC getIsAdmin error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
	createWorkspaceJoined(param = {}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/workspace/create/joined`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => {
					return response.data
				})
				.catch(err=>{
					WorkspaceController_log.error('IPC createPermitWokrspaceInAccount error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';

					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}

	createWorkspace(param={}){
		return windowUtil.isLogin( result => {
			if(result.isLogin){
				param = Object.entries(param).reduce((total, [k,v]) => {
					if(v != undefined && v != ''){
						total[k] = v;
					}
					return total;
				},{});
				return lib_axios.post(`${__serverApi}/api/workspace/create/`, JSON.stringify(param), {
					headers:{
						'Content-Type': 'application/json'
					}
				})
				.then(windowUtil.responseCheck)
				.then(response => response.data)
				.catch(err=>{
					WorkspaceController_log.error('IPC createWorkspace error : ', JSON.stringify(err));
					//axios.defaults.headers.common['Authorization'] = '';
					if(err.response){
						return err.response.data;
					}else{
						return err.message
					}
				})
			}else{
				return {'isLogin': false};
			}
		}).catch(error=>{
			WorkspaceController_log.error('error ::: ', error.message)
			WorkspaceController_log.error('error stack :::', error.stack)
			return undefined;
		})
	}
}
const workspaceController = new WorkspaceController();
;// CONCATENATED MODULE: ./browser/preload/preload.js











/**
 * @author kimjoohyoung
 * @description 기본적으로 사용 할 preload 정의
 */

let permission;

window.addEventListener('load', () => {
	Notification.requestPermission().then(result => {
		console.log(result);
		permission = result;
	})
	if(globalHandler.loadInitRoomId){
		ipcRenderer.webEventSend('roomChange', {roomId:globalHandler.loadInitRoomId});
	}
	if(globalHandler.loadInitNoticeBoardId){
		ipcRenderer.webEventSend('noticeBoardChange', {noticeBoardId:globalHandler.loadInitNoticeBoardId});
	}
})

const globalHandler = new class GlobalHandler{
	#workspaceId;
	loadInitRoomId;
	loadInitNoticeBoardId;
	#option = {};
	constructor(){
		//n : name, v : value, c : create_at, u : update_at
		let queryString = new URLSearchParams(location.search);
		this.#workspaceId = queryString.get('workspaceId');
		this.loadInitRoomId = queryString.get('roomId');
		this.loadInitNoticeBoardId = queryString.get('noticeBoardId');
		this.#option = JSON.parse(localStorage.getItem('o'))?.reduce((t,e)=>{
			t[e.OPTION_NAME] = e;
			return t;
		}, {}) || {};

	}

	resetWorkspace(){
		this.#workspaceId = undefined;
	}

	set workspaceId(workspaceId){
		if(this.#workspaceId === workspaceId) return;
		this.#workspaceId = workspaceId;
		ipcRenderer.webEventSend('workspaceChange', {workspaceId : this.workspaceId});
	}
	get workspaceId(){
		return this.#workspaceId
	}

	setOption({name, value}){
		if( ! this.#option[name]) {
			this.#option[name] = {
				OPTION_NAME : name,
				OPTION_VALUE : value,
				CREATE_AT : new Date().getTime(),
				UPDATE_AT : new Date().getTime(),
			};
		}else{
			this.#option[name].OPTION_VALUE = value,
			this.#option[name].UPDATE_AT = new Date().getTime();
		}
		localStorage.setItem('o', JSON.stringify( Object.values(this.#option)) );
	}

	getOption(name){
		//console.log(this.#option, name);
		return this.#option[name];
	}
}

const ipcRenderer = new class IpcRendererWeb{
	#send = {};
	#invoke = {};
	#connection = {};
	constructor(){

		[
			accountController, apiS3Controller, 
			chattingController, emoticonController, 
			eventStreamController, noticeBoardController,
			roomController, workspaceController
		].flatMap(e=>{
			return Object.getOwnPropertyNames(Object.getPrototypeOf(e)).filter(e=> e != 'constructor').map(name=>{
				return [name, e]
			});
		}).forEach( ([name, clazz])=>{
			this.#send[name] = clazz[name] 
			this.#invoke[name] = clazz[name] 
		})
	}
	send(name, param){
		if( ! this.#send[name] || ! this.#send[name] instanceof Function){
			return;
		}
		this.#send[name](param, undefined, ipcRenderer);
	}
	invoke(name, param){
		if( ! this.#invoke[name] || ! this.#invoke[name] instanceof Function){
			return;
		}
		return this.#invoke[name](param, undefined, ipcRenderer);
	}
	webEventSend(eventName, data){
		if( ! this.#connection[eventName] || ! this.#connection[eventName] instanceof Function){
			return;
		}
		this.#connection[eventName](undefined, data);
	}
	on(eventName, callback){
		this.#connection[eventName] = callback;
	}
	/*
	send : {},
	invoke : {},
	connection : {},
	eventSend: (eventName, data) => {
		if( ! ipcRenderer.connection[eventName] instanceof Function){
			return;
		}
		ipcRenderer.connection[eventName](undefined, data);
	},
	on : (eventName, callback) => {
		connection[eventName] = callback;
	}*/
};

const electronEventTrigger = {
	objectEventListener : {},
	onEvent : {},
	on : (eventName, callBack) => {
		electronEventTrigger.onEvent[eventName] = callBack;
		ipcRenderer.on(eventName, (event, message) => {
			electronEventTrigger.trigger(eventName, event, message);
		})
	},
	addElectronEventListener : (eventName, callBack) => {
		if(electronEventTrigger.objectEventListener.hasOwnProperty(eventName)){
			electronEventTrigger.objectEventListener[eventName].push({ [callBack.name] : callBack });
		}else{
			electronEventTrigger.objectEventListener[eventName] = [{ [callBack.name] : callBack }];
		}
		ipcRenderer.on(eventName, (event, message) => {
			electronEventTrigger.trigger(eventName, event, message);
		})
	},
	removeElectronEventListener : (eventName, callBack) => {
		if(electronEventTrigger.objectEventListener.hasOwnProperty(eventName)){
			if(callBack){
				electronEventTrigger.objectEventListener[eventName] = electronEventTrigger.objectEventListener[eventName].filter(e=> ! e.hasOwnProperty(callBack.name))
			}else{
				delete electronEventTrigger.objectEventListener[eventName]
			}
		}
	},
	trigger : (eventName, event, message) => {
		new Promise(resolve=> {
			if( ! electronEventTrigger.objectEventListener.hasOwnProperty(eventName)){
				resolve();
				return;
			}
			electronEventTrigger.objectEventListener[eventName].forEach(async obj => {
				Object.values(obj).forEach(async callBack => {
					if( ! callBack || ! callBack instanceof Function){
						return;
					}
					new Promise(res=>{	
						try{
							if(eventName == 'checkForUpdates' || eventName == 'updateAvailable' || eventName == 'updateDownloaded'){
								callBack(event,message);
								return;
							}
			
							callBack(message);
						}catch(err){
							console.error(`${eventName} error message ::: `,err.message);
							console.error(`${eventName} error stack ::: `,err.stack);
							throw new Error('');
						}
						res();
					})
				})
			})
			resolve();
		})
		
		new Promise(resolve => {
			if( ! electronEventTrigger.onEvent[eventName] || ! electronEventTrigger.onEvent[eventName] instanceof Function){
				resolve();
				return;
			}
			try{
				electronEventTrigger.onEvent[eventName](message);
			}catch(err){
				console.error(`${eventName} error message ::: `, err.message);
				console.error(`${eventName} error stack ::: `, err.stack);
			}
			resolve();
		})
		
	}
};

const myAPI = {
	/**
	 * 단방향 ipc 통신
	 * 보안상의 이유로ipcRenderer.send 전체 API를 직접 노출하지 않습니다. 
	 * Electron API에 대한 렌더러의 액세스를 가능한 한 많이 제한해야 합니다.
	 */
	//setTitle : (param) => ipcRenderer.send('setTitle', param),
	/**
	 * dialog:IPC 채널 이름 의 접두사는 코드에 영향을 미치지 않습니다. 
	 * 코드 가독성에 도움이 되는 네임스페이스 역할만 합니다.
	 * 보안상의 이유로ipcRenderer.invoke 전체 API를 직접 노출하지 않습니다. 
	 * Electron API에 대한 렌더러의 액세스를 가능한 한 많이 제한해야 합니다. 
	 */
	//openFile : () => ipcRenderer.invoke('dialog:openFile'),
	
	/**
	 * key-value가 아닌 함수는 일렉트론에서만쓰고 웹버전에서는 쓰지 않도록 합니다.
	 * event.electronEventTrigger은 일렉트론 + 웹이 함께써야 하는 부분이 있어 예외입니다.
	 */

	getProjectPath : async () => `${location.origin}/`,

	createSubWindow : async (param) => {
		//open(url?: string | URL, target?: string, features?: string): WindowProxy | null;
		let queryString = Object.entries(param)
			.filter(([k,v]) => v != undefined && v != '' && k != 'pageName' && k != 'pageId')
			.map(([k,v]) => `${k}=${v}`).join('&')
		window.open(`/mobile/create-sub-window/${param.pageName}?${queryString}`, param.pageId)
	},

	closeRequest : async (param) => {},
	
	maximizeRequest : async () => {},

	unmaximizeRequest : async () => {},
	
	minimizeRequest : async () => {},
	
	restoreRequest : async () => {},
	
	isMaximize : async () => false,

	resetWorkspaceId: async () => {
		globalHandler.resetWorkspace();
	},

	getWorkspaceId: async () => {
		return globalHandler.workspaceId;
	},

	notifications: async (param) => {
		let content = param.content.join('\n');
		content = content.substring(0,200) + (content.length > 200 ? '...' : '');
		if(permission == 'granted'){
			myAPI.getProjectPath().then(path=>{
				let noti = new Notification(param.fullName, { body: content, icon: path+'image/icon.icon' });
				noti.onclick = () => {
					if(globalHandler.workspaceId != param.workspaceId){
						ipcRenderer.webEventSend('workspaceChange', {workspaceId : param.workspaceId});
					}
					ipcRenderer.webEventSend('roomChange', {roomId : param.roomId});
				}
				setTimeout(() => {
					noti.close()
				}, 4000);
			})
		}else{
			let html = Object.assign(document.createElement('div'), {
				innerHTML : `
					<h1>${param.fullName}</h1>
					<div>${param.content}</div>
				`
			})
			Object.assign(html.style, {
				position: 'fixed', right: '2vw', bottom: '2vh', borderRadius : '15px',
				background: '#e1e1e1cf', width: '40%', height: 'auto',
				zIndex: 9999, whiteSpace: 'break-spaces', overflowWrap: 'anywhere', 
				display: 'flex', flexDirection: 'column', alignItems: 'center',
				justifyContent: 'flex-start', fontSize: '13px', paddingBottom: '5vh'
			})
			html.onclick = () => {
				if(globalHandler.workspaceId != param.workspaceId){
					ipcRenderer.webEventSend('workspaceChange', {workspaceId : param.workspaceId});
				}
				ipcRenderer.webEventSend('roomChange', {roomId : param.roomId});
			}
			document.body.append(html);
			setTimeout(() => {
				html.remove();
			}, 4000);
		}
	},

	getOption: async (optionName) => globalHandler.getOption(optionName),
	setOption: async (param) => globalHandler.setOption(param),

	isLogin: async () => {
		return windowUtil.isLogin(result=>{
			return {isLogin : result.isLogin}
		})
	},

	getServerUrl : async () => `${location.origin}/login-page`,

	pageChange : {
		changeLoginPage : async () => top.location.href = '/mobile',
		changeWokrspacePage : async () => top.location.href = '/mobile',
		changeMainPage : async (param) => top.location.href = `/mobile/main?workspaceId=${param.workspaceId}`,
	},
	
	event : {
		electronEventTrigger : electronEventTrigger,
	},

	stream : {
		initWorkspaceStream : async (param) => ipcRenderer.send('initWorkspaceStream', param),
	},



	account : {
		loginProcessing : async (param) => ipcRenderer.invoke('loginProcessing', param),
		getAccountInfo : async () => ipcRenderer.invoke('getAccountInfo'),
		updateSimpleAccountInfo : async (param) => ipcRenderer.invoke('updateSimpleAccountInfo', param)
	},

	chatting : {
		sendChatting : async (param) => ipcRenderer.invoke('sendChatting', param),
		deleteChatting : async (param) => ipcRenderer.invoke('deleteChatting', param),
		searchChattingList : async (param) => ipcRenderer.invoke('searchChattingList', param),
	},
	
	workspace : {
		searchWorkspaceMyJoined : async (param) => ipcRenderer.invoke('searchWorkspaceMyJoined', param),
		searchNameSpecificList: async (param) => ipcRenderer.invoke('searchNameSpecificList', param),
		searchWorkspaceInAccount : async (param) => ipcRenderer.invoke('searchWorkspaceInAccount', param),
		getWorkspaceDetail : async (param) => ipcRenderer.invoke('getWorkspaceDetail', param),
		createPermitWokrspaceInAccount: async (param) => ipcRenderer.invoke('createPermitWokrspaceInAccount', param),
		createGiveAdmin : async (param) => ipcRenderer.invoke('createGiveAdmin', param),
		searchPermitRequestList : async (param) => ipcRenderer.invoke('searchPermitRequestList', param),
		getIsAdmin : async (param) => ipcRenderer.invoke('getIsAdmin', param),
		createWorkspaceJoined: async (param) => ipcRenderer.invoke('createWorkspaceJoined', param),
		createWorkspace : async (param) => ipcRenderer.invoke('createWorkspace', param),
		getWorkspaceInAccountCount : async (param) => ipcRenderer.invoke('getWorkspaceInAccountCount', param),
	},

	room : {
		createRoom : async (param) => ipcRenderer.invoke('createRoom', param),
		createMySelfRoom : async (param)=> ipcRenderer.invoke('createMySelfRoom', param),
		createRoomInAccount : async (param) => ipcRenderer.invoke('createRoomInAccount', param),
		createRoomFavorites : async (param) => ipcRenderer.invoke('createRoomFavorites', param),
		updateRoomInAccoutOrder : async (param) => ipcRenderer.invoke('updateRoomInAccoutOrder', param),
		updateRoomFavorites : async (param) => ipcRenderer.invoke('updateRoomFavorites', param),
		searchRoom : async (param) => ipcRenderer.invoke('searchRoom', param),
		searchMyJoinedRoomList : async (param) => ipcRenderer.invoke('searchMyJoinedRoomList', param),
		searchRoomFavoritesList : async (param) => ipcRenderer.invoke('searchRoomFavoritesList', param),
		searchRoomJoinedAccountList : async (param) => ipcRenderer.invoke('searchRoomJoinedAccountList', param),
		getRoomDetail : async (param) => ipcRenderer.invoke('getRoomDetail', param),
		isRoomFavorites : async (param) => ipcRenderer.invoke('isRoomFavorites', param)
	},

	noticeBoard : {
		createNoticeBoard : async (param) => ipcRenderer.invoke('createNoticeBoard', param),
		createNoticeBoardGroup : async (param) => ipcRenderer.invoke('createNoticeBoardGroup', param),
		createNoticeBoardDetail : async (param) => ipcRenderer.invoke('createNoticeBoardDetail', param),
		deleteNoticeBoard : async (param) => ipcRenderer.invoke('deleteNoticeBoard', param),
		deleteNoticeBoardGroup : async (param) => ipcRenderer.invoke('deleteNoticeBoardGroup', param),
		updateNoticeBoardOrder : async (param) => ipcRenderer.invoke('updateNoticeBoardOrder', param),
		updateNoticeBoardDetailOrder : async (param) => ipcRenderer.invoke('updateNoticeBoardDetailOrder', param),
		searchNoticeBoardList : async (param) => ipcRenderer.invoke('searchNoticeBoardList', param),
		searchNoticeBoardDetailList : async (param) => ipcRenderer.invoke('searchNoticeBoardDetailList', param),
	},
	s3: {
		generatePutObjectPresignedUrl : async (param) => ipcRenderer.invoke('generatePutObjectPresignedUrl', param),
		generateGetObjectPresignedUrl : async (param) => ipcRenderer.invoke('generateGetObjectPresignedUrl', param),
	},
	emoticon: {
		createEmotionReaction : async (param) => ipcRenderer.invoke('createEmotionReaction', param),
		getIsReaction : async (param) => ipcRenderer.invoke('getIsReaction', param)
	}

}
window.myAPI = myAPI;
console.log('????????????????');
console.log(window.myAPI)
;// CONCATENATED MODULE: ./view/js/handler/notice_board/NoticeBoardHandler.js

/* harmony default export */ const NoticeBoardHandler = (new class NoticeBoardHandler{
    #noticeBoardId
    #noticeBoard = [];
    #addNoticeBoardIdChangeListener = {};
    #noticeBoardChangeDone;
    #noticeBoardChangeAwait;
    constructor(){

    }

    set addNoticeBoardIdChangeListener({name, callBack, runTheFirst}){
        this.#addNoticeBoardIdChangeListener[name] = callBack;
        if(runTheFirst && this.#noticeBoardId){
            callBack(this);
        }
    }

    get addNoticeBoardIdChangeListener(){
        return this.#addNoticeBoardIdChangeListener;
    }

    set noticeBoardId(noticeBoardId){
        if( ! noticeBoardId){
            console.error('noticeBoardId is undefined');
            return;
        }
        this.#noticeBoard = [];
        this.#noticeBoardId = noticeBoardId;
        let startCallbackPromise = Promise.all(
            Object.values(this.#addNoticeBoardIdChangeListener).map(async callBack => {
                return new Promise(res => {
                    callBack(this);
                    res();
                })
            })
        )
        if( ! this.#noticeBoardChangeAwait){
            this.#noticeBoardChangeAwait = new Promise(resolve=>{
                this.#noticeBoardChangeDone = resolve;
                this.#noticeBoardChangeDone(startCallbackPromise);
            })
        }else{
            this.#noticeBoardChangeAwait.then(() => {
                startCallbackPromise.then(() => {
                    this.#noticeBoardChangeAwait = new Promise(resolve => {
                        this.#noticeBoardChangeDone = resolve;
                    })
                })
            })
        }
    }

    get noticeBoardId(){
        return this.#noticeBoardId;
    }

    get noticeBoard(){
        return this.#noticeBoard;
    }

    removeNoticeBoardIdChangeListener(name){
        delete this.#addNoticeBoardIdChangeListener(name);
    }

});
;// CONCATENATED MODULE: ./view/js/handler/editor/module/ToolHandler.js

class ToolHandler{

	static processingElementPosition(element, target){
		let {x, y, height} = target.getBoundingClientRect();
		
		let elementTop = (y - element.clientHeight)
		let elementLeft = (x - element.clientWidth)
		if(elementTop > 0){
			element.style.top = elementTop + 'px';
		}else{
			element.style.top = y + height + 'px';
		}

		if(elementLeft > 0){
			element.style.left = elementLeft + 'px'
		}else{
			element.style.left = x + width + 'px';
		}
		
	}

	#extendsElement;
	#defaultClass;
	#isInline = true;
	#toolButton;
	#identity;
	#connectedFriends = [];
	#buttonMap = {};
	/**
	 * 
	 * @param {FreedomInterface} identity
	 * @returns {FreedomInterface} 
	 */
	constructor(identity){
		this.#identity = identity;
		//다른 부분 선택시에 동작하지 않도록 수정 필요(에디터만 셀렉션체인지인 경우)
		document.addEventListener("selectionchange", (event) => {
			let selection = window.getSelection();
			if(! document.activeElement.classList.contains('free-will-editor')){
				return;
			}
			/**
			 * None 현재 선택된 항목이 없습니다.
			 * Caret 선택 항목이 축소됩니다(예: 캐럿이 일부 텍스트에 배치되지만 범위가 선택되지 않음).
			 * Range 범위가 선택되었습니다.
			 */
			if(selection.type == 'None' || ! this.#toolButton){
				return;
			}

			let findTarget = this.#connectedFriends.find(e=> selection.containsNode(e, true) || selection.containsNode(e, false))
			if(findTarget){
				this.#toolButton.dataset.tool_status = 'connected';
			}else {
				this.#toolButton.dataset.tool_status = 'blur';
			}
		});
	}

	processingElementPosition(element, target = this.#toolButton){
		ToolHandler.processingElementPosition(element, target);
	}

	isLastTool(tool){
		if(this.#identity.prototype.isPrototypeOf(tool)){
			return tool === this.#connectedFriends.at(-1);
		}else{
			throw new Error(`tool is not my identity, this tool name is ${tool.constructor.name}. but my identity name is ${this.#identity.name}`);
		}
		
	}

	/**
	 * @param {String}
	 */
	set extendsElement(extendsElement){
		this.#extendsElement = extendsElement;
	}
	
	get extendsElement(){
		return this.#extendsElement;
	}
	
	set defaultClass(defaultClass){
		this.#defaultClass = defaultClass;
	}

	get defaultClass(){
		return this.#defaultClass;
	}

	/**
	 * @param {boolean} isInline 
	 */
	set isInline(isInline){
		this.#isInline = isInline;
	}
	
	/**
	 * @returns {boolean}
	 */
	get isInline(){
		return this.#isInline
	}

	set toolButton(toolButton){
		if( ! toolButton || ! toolButton.nodeType || toolButton.nodeType != Node.ELEMENT_NODE){
			throw new Error('toolButton is not element');
		}
		this.#toolButton = toolButton;

	}
	
	get toolButton(){
		return this.#toolButton;
	}

	/**
	 * @param {FreedomInterface}
	 */
	set connectedFriends(friend){
		if(friend.constructor != this.#identity){
			new TypeError('is not my friend')
		}
		
		if(friend.isConnected){
			this.#connectedFriends.push(friend);
		}else{
			this.#connectedFriends = this.#connectedFriends.filter(e=>e.isConnected);
		}
	}

	get connectedFriends(){
		return this.#connectedFriends;
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/module/FreedomInterface.js


class FreedomInterface extends HTMLElement {

	static globalMouseEvent = undefined;
	static lastClickElementPath = undefined;
	static globalClickEventPromiseResolve;
	static globalClickEventPromise = new Promise(resolve=>{
		this.globalClickEventPromiseResolve = resolve;
	});
	static globalKeydownEventPromiseResolve;
	static globalKeydownEventPromise = new Promise(resolve=>{
		this.globalKeydownEventPromiseResolve = resolve;
	})
	static globalSelectionChangeEventPromiseResolve;
	static globalSelectionChangeEventPromise = new Promise(resolve=>{
		this.globalSelectionChangeEventPromiseResolve = resolve;
	})
	static{
		document.addEventListener('mousemove', (event) => {
			//mousePos = { x: event.clientX, y: event.clientY };
			//mousePosText.textContent = `(${mousePos.x}, ${mousePos.y})`;
			this.globalMouseEvent = event;
		});
		document.addEventListener('mousedown', (event) => {
			this.lastClickElementPath = event.composedPath();
			this.globalClickEventPromiseResolve(event)
			this.globalClickEventPromise = new Promise(resolve => {
				this.globalClickEventPromiseResolve = resolve;
			})
		})
		window.addEventListener('keydown', (event) => {
			this.globalKeydownEventPromiseResolve(event);
			this.globalKeydownEventPromise = new Promise(resolve=>{
				this.globalKeydownEventPromiseResolve = resolve;
			})
		})
		document.addEventListener('selectionchange', (event) => {
			this.globalSelectionChangeEventPromiseResolve(event);
			this.globalSelectionChangeEventPromise = new Promise(resolve=>{
				this.globalSelectionChangeEventPromiseResolve = resolve;
			})
		});

	}
	static isMouseInnerElement(element){
		if( ! this.globalMouseEvent) return;
		let {clientX, clientY} = this.globalMouseEvent;
		let {x, y, width, height} = element.getBoundingClientRect();
		let isMouseInnerX = ((x + width) >= clientX && x <= clientX);
		let isMouseInnerY = ((y + height) >= clientY && y <= clientY);
		return (isMouseInnerX && isMouseInnerY);
	}
	static globalKeydownEventListener(element, callBack = ({oldEvent, newEvent})=>{}){
		let oldEvent = undefined;
		let newEvent = undefined;
		const simpleObserver = () => {
			this.globalKeydownEventPromise.then((event)=>{
				newEvent = event;
				callBack({oldEvent, newEvent});
				oldEvent = event;
				simpleObserver();
			})
		}
		simpleObserver();
	}
	static globalSelectionChangeEventListener(element, callBack = ({oldEvent, newEvent})=>{}){
		let oldEvent = undefined;
		let newEvent = undefined;
		const simpleObserver = () => {
			this.globalSelectionChangeEventPromise.then((event)=>{
				newEvent = event;
				callBack({oldEvent, newEvent});
				oldEvent = event;
				simpleObserver();
			})
		}
		simpleObserver();
	}
	/**
	 * 
	 * @param {HTMLElement} element 
	 * @param {Function} callBack 
	 */
	static outClickElementListener(element, callBack = ({oldEvent, newEvent, isMouseOut = false})=>{}){

		if(element == undefined || element?.nodeType != Node.ELEMENT_NODE){
			throw new Error('element is not Element');
		}

		let oldEvent = undefined;
		let newEvent = undefined;
		const simpleObserver = () => {
			this.globalClickEventPromise.then((event)=>{
				let isMouseOut = ! this.isMouseInnerElement(element);
				newEvent = event;
				callBack({oldEvent, newEvent, isMouseOut});
				oldEvent = event;
				simpleObserver();
			})
		}
		simpleObserver();
	}
	
	static DeleteOption = class DeleteOption{
		static #DeleteOptionEnum = class DeleteOptionEnum{
			value;
			constructor(value){
				this.value = value;
				Object.freeze(this);
			}
		}
		static EMPTY_CONTENT_IS_DELETE = new this.#DeleteOptionEnum('empty_content_is_delete');
		static EMPTY_CONTENT_IS_NOT_DELETE = new this.#DeleteOptionEnum('empty_content_is_not_delete');
		value;
		static{
			Object.freeze(this);
		}
		constructor(value){
			this.value = value;
			Object.freeze(this);
		}
	}

	#isLoaded = false;
	Tool;
	#connectedAfterCallback = () => {}
	#connectedAfterOnlyOneCallback = ()=> {}
	#disconnectedAfterCallback = ()=> {}
	#connectedChildAfterCallback = () => {}
	#disconnectedChildAfterCallback = () => {}
	#deleteOption;
	parentEditor;
	#childListObserver = new MutationObserver( (mutationList, observer) => {
		mutationList.forEach((mutation) => {
			let {addedNodes, removedNodes} = mutation;
			let connectedChildPromise = new Promise(resolve => {
				if(addedNodes.length != 0){
					
					let resultList;
					if( ! this.constructor.toolHandler.isInline){
						let lastItemIndex;
						resultList = [...addedNodes].map((e,i)=>{
							if( ! e.classList?.contains(Line.toolHandler.defaultClass)){
								let lineElement = this.parentEditor.createLine();
								lineElement.replaceChildren(e);
								this.append(lineElement);
								if( i == addedNodes.length - 1){
									lastItemIndex = i;
								}
								return lineElement;
							}
							return e;
						});

						//if(lastItemIndex){
						//	resultList[lastItemIndex].line.lookAtMe();
						//}
					}else{
						resultList = addedNodes;
					}
					
					this.connectedChildAfterCallback(resultList);
					//this.connectedChildAfterCallback(addedNodes);
				}
				resolve();
			})
			
			let disconnectedChildPromise = new Promise(resolve => {
				if(removedNodes.length != 0){
					this.disconnectedChildAfterCallback(removedNodes);
				}
				resolve();
			})
			
		})
	});
	constructor(Tool, dataset, {deleteOption = FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_DELETE} = {}){
		super();
		this.#childListObserver.disconnect();
		this.#childListObserver.observe(this, {childList:true})
		this.#deleteOption = deleteOption;
		this.Tool = Tool;
		this.classList.add(this.constructor.toolHandler.defaultClass)

		document.addEventListener('selectionchange', this.removeFun, true);

		if(dataset){
			Object.assign(this.dataset, dataset);
		}
		
		FreedomInterface.globalSelectionChangeEventListener(this, ({oldEvent, newEvent}) => {
			if(this.#deleteOption != FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE && this.isToolEmpty()){
				let thisLine = this.parentEditor?.getLine(this);
				this.remove();
				if(thisLine){
					thisLine.line.lookAtMe();
				}
			}
		})

		if( ! this.constructor.toolHandler.isInline){
			FreedomInterface.globalKeydownEventListener(this, ({oldEvent, newEvent}) => {
				if(this.isCursor() && newEvent.key == 'ArrowDown' && ! this.parentLine.nextElementSibling){
					let nextLine = this.parentEditor.createLine();
					nextLine.line?.lookAtMe();
				}
			})
		}
	}
	connectedCallback(){

		if( ! this.#isLoaded){
			
			this.#isLoaded = true;
			this.constructor.toolHandler.connectedFriends = this;
			this.parentEditor = this.closest('.free-will-editor');
			this.parentLine = this.parentEditor?.getLine(this);

			if(this.childNodes.length == 0 && this.#deleteOption == FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE && (this.innerText.length == 0 || (this.innerText.length == 1 && this.innerText.charAt(0) == '\n'))){
				let sty = window.getComputedStyle(this);
				if(sty.visibility != 'hidden' || sty.opacity != 0){
					this.innerText = '\n';
				}
			}

			if(this.#deleteOption == FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_DELETE && (this.isToolEmpty() || this.childNodes.length == 0)){
				let thisLine = this.parentEditor.getLine(this);
				this.remove();
				if(thisLine){
					thisLine.line.lookAtMe();
				}
			}
						
			if(this.shadowRoot){
				//this.parentLine.prepend(document.createTextNode('\u00A0'));
				//this.parentLine.prepend(document.createElement('br'));
				//this.before(document.createElement('br'));
				//this.parentLine.before(this.parentEditor.createLine());
				let nextLine = this.parentEditor.getNextLine(this.parentLine);
				if( ! nextLine){
					this.parentEditor.createLine().innerText = '\n';
				}else{
					nextLine.line.lookAtMe();
				}
				/*this.childNodes.forEach(e=>{
					if(e.nodeType == Node.TEXT_NODE){
						e.remove()
					}
				})*/
			}

			this.connectedAfterOnlyOneCallback();

			
			/*
			if(this.shadowRoot && (this.querySelectorAll('[slot]').length == 0 || this.childNodes.length == 0 || (this.childNodes.length == 1 && this.childNodes[0]?.tagName == 'BR'))){
				//this.parentLine.prepend(document.createElement('br'));
				
				let slot = Object.assign(document.createElement('slot'),{
					name: 'empty-slot'
				});
				let emptySpan = Object.assign(document.createElement('span'), {
					slot : 'empty-slot'
				});
				//emptySpan.style.opacity='0';
				//emptySpan.append(document.createTextNode('\u200B'));
				emptySpan.innerText = '\n'
				this.prepend(emptySpan);
				this.shadowRoot.append(slot);
				
			}
			*/

			return;
		}
		this.connectedAfterCallback();
	}
	disconnectedCallback(){
        this.#isLoaded = false;
		try{
			this.disconnectedAfterCallback();
		}catch(err){
			console.error(err)
		}finally{
			this.constructor.toolHandler.connectedFriends = this;
			this.#childListObserver.disconnect(this, {childList:true})
			document.removeEventListener('selectionchange', this.removeFun, true);
		}
	}

	isToolEmpty(){
		let sty = window.getComputedStyle(this);
		if(sty.visibility == 'hidden' || sty.opacity == 0){
			return false;
		}
        return this.innerText.length == 0 || (this.innerText.length == 1 && (this.innerText == '\n' || this.innerText == '\u200B'));
    }

	isCursor(){
		let selection = window.getSelection();
		if(! document.activeElement.classList.contains('free-will-editor')){
			return false;
		}
		if(selection.type == 'None'){
			return false;
		}

		return selection.containsNode(this, true) || selection.containsNode(this, false)
	}

	/**
	 * @param {Function}
	 */
	set connectedAfterCallback(connectedAfterCallback){
		this.#connectedAfterCallback = connectedAfterCallback;
	}

	/**
	 * @returns {Function}
	 */
	get connectedAfterCallback(){
		return this.#connectedAfterCallback;
	}

	/**
	 * @param {Function}
	 */
	set connectedAfterOnlyOneCallback(connectedAfterOnlyOneCallback){
		this.#connectedAfterOnlyOneCallback = connectedAfterOnlyOneCallback;
	}
	
	/**
	 * @returns {Function}
	 */
	get connectedAfterOnlyOneCallback(){
		return this.#connectedAfterOnlyOneCallback;
	}

	/**
	 * @param {Function}
	 */
	set disconnectedAfterCallback(disconnectedAfterCallback){
		this.#disconnectedAfterCallback = disconnectedAfterCallback;
	}

	/**
	 * @returns {Function}
	 */
	get disconnectedAfterCallback(){
		return this.#disconnectedAfterCallback;
	}
 
	/**
	 * @param {Function}
	 */
	set connectedChildAfterCallback(connectedChildAfterCallback){
		this.#connectedChildAfterCallback = connectedChildAfterCallback;
	}

	/**
	 * @returns {Function}
	 */
	get connectedChildAfterCallback(){
		return this.#connectedChildAfterCallback;
	}

	/**
	 * @param {Function}
	 */
	set disconnectedChildAfterCallback(disconnectedChildAfterCallback){
		this.#disconnectedChildAfterCallback = disconnectedChildAfterCallback;
	}

	/**
	 * @returns {Function}
	 */
	get disconnectedChildAfterCallback(){
		return this.#disconnectedChildAfterCallback;
	}
}
;// CONCATENATED MODULE: ./view/js/handler/editor/component/Line.js


class Line {
	#isLoaded = false;
	#prevParent;

	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-line'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-line';
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = ``
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	/**
	 * 
	 * @param {HTMLElement} element 
	 * @returns {HTMLElement} 
	 */
	static getLine(element){
		let line = undefined;
		if( ! element.parentElement){
			return line;
		}else if(element.classList?.contains(Line.toolHandler.defaultClass)){
			return element;
		}else if(element.parentElement.classList.contains(Line.toolHandler.defaultClass)){
			line = element.parentElement;
		}else{
			line = element.parentElement.closest(`.${Line.toolHandler.defaultClass}`);
		}
		return line;
	}
	static getTool(element, TargetTool){
		let tool = undefined;
		
		if( ! element.parentElement){
			return tool;
		}else if(element.classList?.contains(TargetTool.toolHandler.defaultClass)){
			return element
		}else if(element.parentElement.classList.contains(TargetTool.toolHandler.defaultClass)){
			tool = element.parentElement;
		}else{
			tool = element.parentElement.closest(`.${TargetTool.toolHandler.defaultClass}`);
		}

		if(! tool){
			tool = element.parentElement.querySelector(`.${TargetTool.toolHandler.defaultClass}`);
		}
		return tool;
	}
	
	static isElementTextEmpty(element){
        let sty = window.getComputedStyle(element);
		if(sty.visibility == 'hidden' || sty.opacity == 0){
			return false;
		}
		//let emptyString = (element.innerText.replace(/\n+/g, '\n') == '\n' || element.innerText.replace(/\u200B+/g, '\u200B') == '\u200B');
        return element.innerText.length == 0 || (element.innerText.length == 1 && (element.innerText == '\n' || element.innerText == '\u200B'));
    }

	lineElement

	constructor(div){
		//super();
		if( ! div){
			this.lineElement = Object.assign(document.createElement('div'),{
				className : Line.toolHandler.defaultClass
			})
		}else{
			this.lineElement = div;
			this.lineElement.classList.add(Line.toolHandler.defaultClass);
		}
		this.lineElement.dataset.is_line = '';
		/*
		Object.getOwnPropertyNames(Object.getPrototypeOf(this)).forEach(functionName=>{
			if(functionName == 'constructor') return;
			this.lineElement[functionName] = this[functionName];
		});
		*/
		this.lineElement.line = this;
		/*
		this.onkeyup = (event) => {
			if(event.key === 'Backspace' && this.innerText.length == 1 && (this.innerText.includes)){

			}
		}
		*/
		/*
		let observer = new MutationObserver( (mutationList, observer) => {
			mutationList.forEach((mutation) => {
				if(Line.isElementTextEmpty(this.lineElement)){
					if(this.lineElement.childElementCount != 0){
						return;
					}
					//this.lineElement.innerText = '\n';
					window.getSelection().setPosition(this.lineElement, 0)
					this.lineElement.focus();
				}
			})
		});
		observer.observe(this.lineElement, {
			childList:true,
			subtree: true
		})
		*/
	}
	/*
	connectedCallback(){
		if( ! this.#isLoaded){
            this.#isLoaded = true;

			
			if(this.innerText.length == 0 || (this.innerText.length == 1 && this.innerText.charAt(0) == '\n')){
				this.innerText = '\n';
				window.getSelection().setPosition(this, 1)
				this.focus();
			}

		}
	}
	disconnectedCallback(){
        this.#isLoaded = false;
    }
	*/
	/**
	 * applyTool, cancelTool에서 3가지 경우의 수로 함수를 분기시킬 것 apply mng와 cancel mng class를 새로 만들것, 
	 * line이 할 일이 아니니 freedomPlusEditor로 옮길 것
	 * --applyTool
	 * 1. 범위 전체
	 * 2. 범위이되 line이 같을 때 (textNode별로 처리 필요)
	 * 3. start와 end가 다를 때(멀티라인)
	 * --cancelTool
	 * 1. 범위
	 * 2. 범위 중 일부
	 * 3. 멀티라인
	 */

	/**
	 * tool이 좌우 옆 또는 자식, 부모에서 동일한 tool임을 감지할 때 합치는 로직 필요
	 */
	/**
	 * 
	 * @param {*} tool 
	 * @param {*} range 
	 * @returns 
	 */
	async #applyOnlyOneTool(tool, range){
		return new Promise(resolve=>{
			range.surroundContents(tool);
			resolve(tool)
		});
	}
	
	async #applyOnlyOneLine(range, tool, TargetTool){
		return new Promise(resolve=>{
			let {startOffset, endOffset, startContainer,endContainer} = range;

			let selection = window.getSelection();

			let startNodeToPrevNodeIndex = [...this.lineElement.childNodes].findIndex(e=>selection.containsNode(e, true)) - 1;
			let isFirstNodeToStart = startNodeToPrevNodeIndex < 0;

			
			let nodeList = [];
			for(let i = startNodeToPrevNodeIndex + 1 ; true ; i += 1){
				if( ! this.lineElement.childNodes[i] || ! selection.containsNode(this.lineElement.childNodes[i], true)){
					break;
				}
				let iter = document.createNodeIterator(this.lineElement.childNodes[i], NodeFilter.SHOW_TEXT)
				let targetNode;
				while(targetNode = iter.nextNode()){
					if(targetNode == endContainer){
						// this.lineElement.childNodes[i] == endContainer == targetNode
						let clone = targetNode.cloneNode(true);
						clone.textContent = targetNode.textContent.substring(endOffset);
						targetNode.after(clone);
						targetNode.textContent = targetNode.textContent.substring(0, endOffset);
					}
				}
				nodeList.push(this.lineElement.childNodes[i]);
			}
			//nodeList.forEach(e=>e.remove())
			//prepend
			tool.append(...nodeList);
			if(isFirstNodeToStart){
				this.lineElement.prepend(tool)
			}else{
				let startTarget = this.lineElement.childNodes[startNodeToPrevNodeIndex];
				startTarget.after(tool);
			}
			
			/*
			range.setStart(startContainer, startOffset);
			range.setEnd(startContainer, startContainer.textContent.length);
			range.surroundContents(tool);
			
			let targetNode = startContainer.nextSibling;
			//let lastPrevTool = undefined;
			while(targetNode){
				console.log(targetNode)
				if(targetNode == endContainer){
					console.log(2)
					break;
				}
				if(targetNode.textContent == '' || TargetTool.prototype.isPrototypeOf(targetNode)){
					console.log(3)
					targetNode = targetNode.nextSibling
					continue;
				}

				console.log(targetNode);
				range.selectNode(targetNode);
				range.surroundContents(new TargetTool());
				console.log(4)
				targetNode = targetNode.nextSibling
			}

			let endTool = new TargetTool();
			range.setStart(endContainer, 0);
			range.setEnd(endContainer, endOffset);
			range.surroundContents(endTool);
			resolve(endTool);
			*/
			resolve();
		});
	}
	async #applyMultipleLineAllBlock(range, tool ,TargetTool, endLine){
		return new Promise(resolve => {
			let fragment = range.extractContents();
			tool.append(...fragment.childNodes);
			this.lineElement.append(tool);
			resolve(tool);
		})
	}
	async #applyMultipleLineAll(range, tool, TargetTool, endLine){
		return new Promise(resolve=>{
			let {startOffset, endOffset, startContainer,endContainer} = range;
			
			range.setStart(startContainer, startOffset);
			range.setEnd(startContainer, startContainer.textContent.length);
			range.surroundContents(tool);

			let targetStartLineItem = startContainer.nextSibling?.nextSibling;
			
			if(targetStartLineItem){
				let itemRemoveList = [];
				let itemAppendList = [];
				while(targetStartLineItem){
					if(targetStartLineItem.nodeType == Node.ELEMENT_NODE){
						if(tool == targetStartLineItem){
							break;
						}
						tool.append(targetStartLineItem);
						
					}else{
						itemAppendList.push(targetStartLineItem.textContent);
						itemRemoveList.push(targetStartLineItem);
					}
					targetStartLineItem = targetStartLineItem.nextSibling;	

				}
				[...tool.childNodes].find(e=>e.nodeType == Node.TEXT_NODE)?.appendData(itemAppendList.join(''));
				itemRemoveList.forEach(e=>e.remove())
			}
			
			if(tool.parentElement != this.lineElement && tool.parentElement?.nextSibling){
				//20230817 멀티라인일시 첫번쨰 라인 중첩 서식 미동작 현상 수정을 위해 내용 변경
				let nextTool = new TargetTool();
				nextTool.append(tool.parentElement.nextSibling);
				tool.parentElement.after(nextTool);
				let nextItem = nextTool.nextSibling;

				while(nextItem){		
					if(nextTool == nextItem){
						break;
					}
					nextTool.append(nextItem);

				}
			}
			
			
			let targetLine = Line.getLine(startContainer).nextElementSibling; 
			let middleTargetTool;
			
			while(targetLine){
				if(targetLine === endLine){
					break;
				}
				// 아래 주석 지우지 말 것, 중첩 자식 요소에서 미동작 또는 오류 발생시 아래 로직 주석 풀고 테스트 해볼 것
				// let middleTool = new TargetTool();
				// middleTool.append(...targetLine.childNodes);
				// targetLine.append(middleTool);
				middleTargetTool = new TargetTool()
				range.selectNodeContents(targetLine);
				range.surroundContents(middleTargetTool);
				targetLine = targetLine.nextElementSibling;

			}

			if(Line.prototype.isPrototypeOf(endContainer.line)){
				resolve(( ! middleTargetTool ? tool : middleTargetTool ));
			}else{
				let endTool = new TargetTool();
				range.setStart(endContainer, 0);
				range.setEnd(endContainer, endOffset);
				range.surroundContents(endTool);
				// 분할 적용 되지 않도록 합친다 ex(<b>1</b><b>2</b> => <b>12</b>) 
				let targetEndLineItem = endContainer.previousSibling;
				if(targetEndLineItem){
					let itemRemoveList = [];
					let itemAppendList = [];

					while(targetEndLineItem){
						if(targetEndLineItem.nodeType == Node.ELEMENT_NODE){
							endTool.prepend(targetEndLineItem);
							/*
							let firstTextNode = [...targetEndLineItem.childNodes].find(e=>e.nodeType == Node.TEXT_NODE);
							let moveTextList = [...endTool.childNodes].filter(e=>e.nodeType == Node.TEXT_NODE)
							moveTextList.forEach(e=>{
								e.textContent = firstTextNode.textContent + e.data
								firstTextNode.remove();
								firstTextNode = e;
							})
							targetEndLineItem.append(firstTextNode);
							*/
						}else{
						//endTool.prepend(document.createTextNode(targetEndLineItem.textContent));
							itemRemoveList.push(targetEndLineItem);
							itemAppendList.unshift(targetEndLineItem.textContent)
						}
						targetEndLineItem = targetEndLineItem.previousSibling;
					}
					//let targetTextNode = [...endTool.childNodes].find(e=>e.nodeType == Node.TEXT_NODE)
					//itemAppendList.push(targetTextNode.data);
					//targetTextNode.textContent = itemAppendList.join('');
					//endTool.append(targetTextNode)
					[...endTool.childNodes].find(e=>e.nodeType == Node.TEXT_NODE)?.appendData(itemAppendList.join(''));
					//endTool.prepend(document.createTextNode(itemAppendList.join('')))
					//itemRemoveList.forEach(e=>e.remove())
				}
				resolve(endTool);
			}


		})
	}

	/**
	 * 
	 * @param {*} TargetTool 
	 * @param {*} range 
	 * @returns 
	 */
	async applyTool(TargetTool, range, endLine){
		return new Promise(resolve => {
			let tool = new TargetTool();
			let {startOffset, endOffset, startContainer,endContainer} = range;

			/*
			if(startContainer === endContainer){
				console.log('applyOnlyOneTool');
				this.#applyOnlyOneTool(tool, range).then(tool=>{
					resolve(tool)
				})
			}*/
			if(this.lineElement.childNodes.length == 1 && this.lineElement.innerText == '\n' && this.lineElement.childNodes[0].nodeName == 'BR'){
				this.lineElement.childNodes[0].remove();
			}
			if(tool.shadowRoot || ! TargetTool.toolHandler.isInline){
				resolve(this.#applyMultipleLineAllBlock(range, tool, TargetTool, endLine));
			}else if(startContainer === endContainer && this.lineElement.innerText.length != range.toString().length){
				console.log('applyOnlyOneTool');
				this.#applyOnlyOneTool(tool, range).then(tool=>{
					resolve(tool)
				})
			}else if(Line.getLine(startContainer) === Line.getLine(endContainer)){
				console.log('applyOnlyOneLine')
				this.#applyOnlyOneLine(range, tool, TargetTool).then(endTool => {
					resolve(endTool)
				})
			}else{
				console.log('applyMultipleLineAll')
				this.#applyMultipleLineAll(range, tool, TargetTool, endLine).then(endTool => {
					resolve(endTool)
				})
			}
		})
	}

	async #cancelOnlyOneLine(range, tool, TargetTool){
		return new Promise(resolve => {
			// this로 childern 돌려서 TargetTool 타입 체크랑 nodeType로 바깥으로 빼는 로직 만들기
			let {startOffset, endOffset, startContainer, endContainer} = range;
			this.lineElement.childNodes.forEach(e=>{
				this.#findCancels(e, TargetTool);
			});
			resolve();
		});
	}
	async #cancelOnlyOneTool(range, tool, TargetTool){
		return new Promise(resolve => {
			let {startOffset, endOffset, startContainer, endContainer, commonAncestorContainer} = range;
			/*
			let textNode = document.createTextNode(startContainer.textContent.substring(startOffset, endOffset));
			let startNextSibling = (tool?.nextSibling  || endContainer.nextSibling);
			let startPrevSibling = (tool?.previousSibling || startContainer.previousSibling);
			*/
			let offset = endOffset - startOffset;

			let leftText = undefined;
			let rightText = undefined;
			let leftList = [];
			let rightList = [];
			let targetWrap = undefined;
			let targetText;
			if(commonAncestorContainer.nodeType == Node.TEXT_NODE && commonAncestorContainer.parentElement != this && ! TargetTool.prototype.isPrototypeOf(commonAncestorContainer.parentElement)){
				let cloneRange = range.cloneRange();
				cloneRange.selectNode(commonAncestorContainer.parentElement);
				targetText = cloneRange.cloneContents();
			}else{
				targetText = range.cloneContents();
			}

			let selection = window.getSelection()

			//if(tool.childNodes.length > 1){
			let list = [...tool.childNodes];
			if( ! TargetTool.toolHandler.isInline){
				try{
					this.lineElement.append(...list);
				}catch(Ignore){
					Line.getLine(tool).append(...list);
				}
				tool.remove();
				resolve();
				return
			}
			let index = list.findIndex(e=> selection.containsNode(e, true) || selection.containsNode(e, false))
			targetWrap = list[index]
			leftList = list.slice(0, index);
			rightList = list.slice(index + 1);
			//}
			

			if(startContainer.textContent.length != offset){
				leftText = startContainer.textContent.substring(0, startOffset);			
				rightText = offset <= 1 ? startContainer.textContent.substring(startOffset + 1) : startContainer.textContent.substring(startOffset + offset);
				if(targetWrap != undefined){
					let cloneLeft = targetWrap.cloneNode(false);
					cloneLeft.textContent = leftText;
					leftText = cloneLeft; 
					let cloneRight = targetWrap.cloneNode(false);
					cloneRight.textContent = rightText;
					rightText = cloneRight; 
					targetWrap.remove();
				}else{
					leftText = document.createTextNode(leftText);
					rightText = document.createTextNode(rightText);
				}
				leftList.unshift(leftText);
				rightList.push(rightText);
			}
			//tool.replaceChildren(...leftList, targetText, ...rightList)

			let leftElement = undefined;
			if(leftList.length != 0){
				leftElement = new TargetTool();
				leftElement.append(...leftList);
			}
			let rightElement = undefined;
			if(rightList.length != 0){
				rightElement = new TargetTool();
				rightElement.append(...rightList);
			}

			let appendList = [leftElement, targetText, rightElement].filter(e=>e);
			if(tool.previousSibling){
				tool.previousSibling.after(...appendList)
			}else if(tool.nextSibling){
				tool.nextSibling.before(...appendList);
			}else{
				this.lineElement.append(...appendList);
			}
			/*
			if(startPrevSibling && startPrevSibling.nodeType == Node.TEXT_NODE){
				console.log(1)
				if(rightText){
					let rightTool = new TargetTool();
					rightTool.textContent = rightText;
					startPrevSibling.after(rightTool);
				}
				startPrevSibling.after(textNode);
				if(leftText){
					let leftTool = new TargetTool()
					leftTool.textContent = leftText 
					startPrevSibling.after(leftTool);
				}
			}else if(startNextSibling && startNextSibling.nodeType == Node.TEXT_NODE){
				console.log(2)
				if(leftText){
					let leftTool = new TargetTool()
					leftTool.textContent = leftText 
					startNextSibling.before(leftTool);
				}
				startNextSibling.before(textNode);
				if(rightText){
					let rightTool = new TargetTool();
					rightTool.textContent = rightText;
					startNextSibling.before(rightTool);
				}
			}else{
				console.log(3)
				if(leftText){
					let leftTool = new TargetTool()
					leftTool.textContent = leftText 
					this.append(leftTool);
				}
				this.append(document.createTextNode(textNode));
				if(rightText){
					let rightTool = new TargetTool();
					rightTool.textContent = rightText;
					this.append(rightTool);
				}
			}*/
			//startContainer.remove();
			tool.remove();
			resolve();
		});
	}

	async #cancelMultipleLineAll(range, tool, TargetTool, endLine){
		return new Promise(resolve => {
			let {startOffset, endOffset, startContainer, endContainer} = range;
			
			let endTool = Line.getTool(endContainer, TargetTool);

			// 파이어폭스에서 startContainer, endContainer가 나노 미세 컨트롤로 커서 위치와 관계 없이 비정상 동작 하는 현상 수정 필요
			let startTextNode = document.createTextNode(startContainer.textContent.substring(startOffset, startContainer.textContent.length));
			let endTextNode = document.createTextNode(endContainer.textContent.substring(0, endOffset));
			
			let startLeftText = startContainer.textContent.substring(0, startOffset);
			let endRightText = endContainer.textContent.substring(endOffset, endContainer.textContent.length)

			let startNextSibling = tool?.nextSibling
			let startPrevSibling = tool?.previousSibling

			let endNextSibling = endTool?.nextSibling
			let endPrevSibling = endTool?.previousSibling

			if(startOffset == 0){
				//console.log(1)
				this.#findCancels(this.lineElement, TargetTool);
			}else
			if(startPrevSibling && startPrevSibling.nodeType == Node.TEXT_NODE){
				//console.log(2)
				startPrevSibling.after(startTextNode);
				if(startLeftText){
					let leftTool = new TargetTool()
					leftTool.textContent = startLeftText 
					startPrevSibling.after(leftTool);
				}
			}else if(startNextSibling && startNextSibling.nodeType == Node.TEXT_NODE){
				//console.log(3)
				if(startLeftText){
					let leftTool = new TargetTool()
					leftTool.textContent = startLeftText 
					startNextSibling.before(leftTool);
				}
				startNextSibling.before(startTextNode);
			}else{
				/*console.log(4)
				if(startLeftText){
					let leftTool = new TargetTool()
					leftTool.textContent = startLeftText 
					this.append(leftTool);
				}
				this.append(startTextNode);
				*/
			}
			if(tool){
				tool.remove();
			}

			let nextLine = this.lineElement.nextElementSibling;


			while(nextLine){
				if(nextLine == endLine){
					break;
				}
				nextLine.childNodes.forEach(e=>{
					//console.log(4);
					this.#findCancels(e, TargetTool);
				})
				nextLine = nextLine.nextElementSibling;

			}

			if(endOffset == endContainer.length){
				//console.log(1)
				this.#findCancels(endLine, TargetTool);
			}else
			if(endPrevSibling && endPrevSibling.nodeType == Node.TEXT_NODE){
				//console.log(2)
				if(endRightText){
					let rightTool = new TargetTool();
					rightTool.textContent = endRightText;
					endPrevSibling.after(rightTool);
				}
				endPrevSibling.after(endTextNode);
			}else if(endNextSibling && endNextSibling.nodeType == Node.TEXT_NODE){
				//console.log(3)
				endNextSibling.before(endTextNode);
				if(endRightText){
					let rightTool = new TargetTool();
					rightTool.textContent = endRightText;
					endNextSibling.after(rightTool);
				}
			}else{
				/*console.log(4)
				if(endRightText){
					let rightTool = new TargetTool();
					rightTool.textContent = endRightText;
					//endLine.append(rightTool);
				}
				endLine.prepend(endTextNode);
				*/
			}

			if(endTool){
				endTool.remove();
			}
			resolve();
		});
	}

	async cancelTool(TargetTool, selection, endLine){
		return new Promise(resolve => {
			let {isCollapsed, anchorNode, anchorOffset} = selection;
			let range = selection.getRangeAt(0);
			let {startOffset, endOffset, startContainer, endContainer, commonAncestorContainer} = range;
			let tool = Line.getTool(startContainer, TargetTool);
			/*
			if(startContainer === endContainer){
				if(startContainer.parentElement.childNodes.length == 1 
					&& startContainer.parentElement.childNodes[0] ==  startContainer
					&& startContainer.parentElement.childNodes[0] ==  endContainer
					&& endContainer.textContent.length == endOffset){
					// tool 범위 전체 선택인 경우
					this.#cancelOnlyOneLine(range, tool, TargetTool).then(()=>{
						console.log('cancelOnlyOneLine 1')
						resolve();
					})
				}else{
					// 범위 중 일부
					this.#cancelOnlyOneTool(range, tool, TargetTool).then(()=>{
						console.log('cancelOnlyOneTool')
						resolve();
					})
				}
			*/

			if( ! tool){
				TargetTool.toolHandler.toolButton.dataset.tool_status = 'connected'
				resolve();
			}else{
				if( ! TargetTool.toolHandler.isInline || (startContainer === endContainer && this.lineElement.innerText.length != range.toString().length ) ){
					this.#cancelOnlyOneTool(range, tool, TargetTool).then(()=>{
						console.log('cancelOnlyOneTool')
						resolve();
					})
				}else if(Line.getLine(startContainer) === Line.getLine(endContainer)){
					// 하나만
					// tool 범위 전체 선택인 경우
					this.#cancelOnlyOneLine(range, tool, TargetTool).then(()=>{
						console.log('cancelOnlyOneLine 2')
						resolve();
					})
				}else{
					this.#cancelMultipleLineAll(range, tool, TargetTool, endLine).then(()=>{
						console.log('cancelMultipleLineAll')
						resolve();
					}).catch(err=>console.error(err))
				}
				resolve();
			}
		})
	}

	#findCancels(element, TargetTool){
		new Promise(res=>{
			if(TargetTool.prototype.isPrototypeOf(element)){
				if(element.nextSibling){
					element.nextSibling.before(...element.childNodes);
				} else if(element.previousSibling){
					element.previousSibling.after(...element.childNodes);
				}else {
					element.parentElement.append(...element.childNodes);
				}
				element.remove();
			}else{
				if(element.nodeType == Node.ELEMENT_NODE){
					element.childNodes.forEach(e=>{
						this.#findCancels(e, TargetTool);
					})
				}
			}
			res();
		})
	}

	lookAtMe(){
		let sty = window.getComputedStyle(this.lineElement);
		if(sty.visibility == 'hidden' || sty.opacity == 0){
			return;
		}
		if(this.lineElement.innerText.length == 0 || (this.lineElement.innerText.length == 1 && this.lineElement.innerText.charAt(0) == '\n')){
			this.lineElement.innerText = '\n';
		}
		if(this.lineElement.isContentEditable){
			this.lineElement.focus()
			window.getSelection().setPosition(this.lineElement, this.lineElement.childNodes.length)
		}
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/module/FreeWiilHandler.js


class FreeWiilHandler extends HTMLElement{
    static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor'
	});

    static #defaultClass = 'free-will-editor';

    static createDefaultStyle(){
        let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle = defaultStyle;
        }
		this.#defaultStyle.textContent = `
            .${this.defaultClass}{
                height: inherit;
                overflow: auto;
                overflow-wrap: anywhere;
                outline: none;
                background-color: inherit;
                padding-top: 0.6%;
                padding-left: 0.5%;
                padding-right: 0.8%;
                -ms-overflow-style: none;
                scrollbar-width: none;
                display: block;
            }
            .${this.#defaultClass}::-webkit-scrollbar {
                display: none;
            }

            .${this.#defaultClass} > :nth-child(1)::before{
                content: attr(data-placeholder);
                position: absolute;
                color: #d1d1d1;
                font-weight: 600;
                font-family: revert;
                cursor: text;
            }
		`
		return this.#defaultStyle;
	}
    static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

    static get defaultClass(){
        return this.#defaultClass;
    }

    static set defaultClass(defaultClass){
        this.#defaultClass = defaultClass;
    }

    constructor({isDefaultStyle = true} = {}){
        super()
        this.classList.add('free-will-editor');

    }

    blockBackspaceEvent(event){
    }

    /**
     * 
     * @returns {HTMLDivElement}
     */
    createLine(){
        let line = new Line();
        this.append(line.lineElement);
        return line.lineElement;
    }

    getLineRange(selection = window.getSelection()){
        return new Promise(resolve => {
            let {anchorNode, focusNode} = selection; 
            let startAndEndLineObject;
            if(anchorNode == this){
                //console.log(111)
                let allLine = [...this.children].filter(e=>e.classList.contains(`${Line.toolHandler.defaultClass}`))
                startAndEndLineObject = {
                    startLine : allLine[0],
                    endLine : allLine.at(-1)
                }
                let range = selection.getRangeAt(0);
                //selection.removeAllRanges();
                let endLineChildNodes = [...startAndEndLineObject.endLine.childNodes].at(-1);
                range.setStart(startAndEndLineObject.startLine.childNodes[0], 1);
                range.setEnd(endLineChildNodes, endLineChildNodes.nodeType == Node.TEXT_NODE ? endLineChildNodes.textContent.length : startAndEndLineObject.endLine.childNodes.length);
                selection.addRange(range);
            }else{
                //console.log(222)
                let anchorNodeLine = Line.getLine(anchorNode);
                let focusNodeLine = Line.getLine(focusNode);
                if(anchorNodeLine == focusNodeLine){
                    //console.log(333)
                    resolve({
                        startLine: anchorNodeLine,
                        endLine: focusNodeLine
                    })
                    return;
                }
                //console.log(4444, anchorNodeLine , focusNodeLine);
                startAndEndLineObject = {startLine : anchorNodeLine, endLine : focusNodeLine};
                /*startAndEndLineObject = [...this.querySelectorAll(`.${Line.toolHandler.defaultClass}`)].reduce((obj,item,index)=>{
                    if(item == anchorNodeLine || item == focusNodeLine){
                        let key = 'startLine';
                        if(obj.hasOwnProperty(key)){
                            obj['endLine'] = item
                        }else{
                            obj[key] = item
                        }
                    }
                    return obj;
                },{})*/
            }
            resolve(startAndEndLineObject);
        })
    }

    isNextLineExist(element){
        let line = Line.getLine(element);

        let nextLine = line.nextElementSibling;
        if( ! nextLine){
            return false;
        }
        return Line.prototype.isPrototypeOf(nextLine.line);
    }

    /**
     * 
     * @param {HTMLElement} element 
     * @param {Object} param1 
     * @returns {HTMLElement}
     */
    getNextLine(element, {focus = false} = {}){
        let line = Line.getLine(element);
        if( ! line){
            return undefined;
        }
        
        let nextLine = line.nextElementSibling;
        if(nextLine && Line.prototype.isPrototypeOf(nextLine.line)){
            if(focus){
                nextLine.line.lookAtMe();
            }
            return nextLine;
        }
        return undefined;
    }
    
    /**
     * 
     * @param {HTMLElement} element 
     * @param {Object} param1 
     * @returns {HTMLElement}
     */
    getPrevLine(element, {focus = false} = {}){
        let line = Line.getLine(element);
        if( ! line){
            return undefined;
        }
        let nextLine = line.previousElementSibling;
        if(nextLine && Line.prototype.isPrototypeOf(nextLine.line)){
            if(focus){
                nextLine.line.lookAtMe();
            }
            return nextLine;
        }
        return undefined;
    }

    getLine(element){
        return Line.getLine(element);
    }

}
;// CONCATENATED MODULE: ./view/js/handler/editor/fragment/UndoManager.js




// 추후 indexed db 사용 검토
class UndoManager{
    #editor;
    #history = [];
    #historyIndex = 0;
    #historyLimit = 50;
    #UndoRedo = class UndoRedo{
        #html
        #time
        constructor(html){
            this.#html = html;
            this.#time = new Date().getTime();
        }
        get html(){
            return this.#html;
        }
        get time(){
            return this.#time;
        }
    }

    #lastCursorPositionRect;
    //#isUndoSwitch = false;

    /**
     * 
     * @param {FreeWillEditor} targetEditor 
     */
    //레인지를 저장해뒀다가 쓰기
    constructor(targetEditor){
        this.#editor = targetEditor;
        //if(this.#editor.contentEditable == 'true'){
            this.addCursorMove();
            this.addUndoKey();
            this.addUserInput();
        //}

    }

    addCursorMove(){
        /*
        let isFirst = true;
        document.addEventListener("selectionchange", (event) => {
            if(document.activeElement !== this.#editor){
				return;
			}
            let selection = window.getSelection();
            let range = selection.getRangeAt(0);
            let newRect = range.getBoundingClientRect();
            if(this.#lastCursorPositionRect && (this.#lastCursorPositionRect.x != newRect.x || this.#lastCursorPositionRect.y != newRect.y)){
                range.insertNode(this.#cursor);
            }else if(isFirst){
                range.insertNode(this.#cursor);
                isFirst = false;
            }
            this.#lastCursorPositionRect = newRect;
        });
        */
        FreedomInterface.globalSelectionChangeEventListener(this, ({oldEvent, newEvent}) => {
            if(document.activeElement !== this.#editor){
				return;
			}
            this.rememberCursor();
		})
    }

    rememberCursor(){
        let selection = window.getSelection();
        let range = selection.getRangeAt(0);
        let newRect = range.getBoundingClientRect();
        if(this.#lastCursorPositionRect && (this.#lastCursorPositionRect.x == newRect.x && this.#lastCursorPositionRect.y == newRect.y)){
            return
        }
        let {focusNode, focusOffset} = selection

        let target;
        let index = (() => {
            if(focusNode.nodeType == Node.TEXT_NODE){
                target = focusNode.parentElement;
                return [...target.childNodes].findIndex(e=>e == focusNode);
            }else if(focusNode.nodeType == Node.ELEMENT_NODE){
                target = focusNode;
                return 0;
            }
        })();

        if(target == undefined){
            return;
        }else{
            let prevCursorTarget;
            if(this.#editor.hasAttribute('is_cursor')){
                prevCursorTarget = this.#editor;
            }else{
                prevCursorTarget = this.#editor.querySelector('[is_cursor]');
            }
            if(prevCursorTarget && prevCursorTarget != target){
                prevCursorTarget.removeAttribute('is_cursor');
                prevCursorTarget.removeAttribute('cursor_offset');
                prevCursorTarget.removeAttribute('cursor_type');
                prevCursorTarget.removeAttribute('cursor_index');
                prevCursorTarget.removeAttribute('cursor_scroll_x');
                prevCursorTarget.removeAttribute('cursor_scroll_y');
            }
        }

        /*
            Object.assign(parent.dataset,{
                offset: focusOffset,
                index: childIndex
            })
        */
        target.setAttribute('is_cursor', '');
        target.setAttribute('cursor_offset', focusOffset);
        target.setAttribute('cursor_type', target.nodeType);
        target.setAttribute('cursor_index', index);
        target.setAttribute('cursor_scroll_x', this.#editor.scrollLeft);
        target.setAttribute('cursor_scroll_y', this.#editor.scrollTop);
        this.#lastCursorPositionRect = newRect;
    }

    addUndoKey(){
        this.#editor.addEventListener('keydown', (event) => {
            if(document.activeElement !== this.#editor){
				return;
			}
            let {ctrlKey, key} = event;
            if( ! ctrlKey || ! (key == 'z' || key == 'y')){
                return;
            }
            event.preventDefault();
            if(key == 'z'){
                this.undoKeyEvent();
            }else{
                this.redoKeyEvent();
            }

            let cursorTarget = this.#editor.querySelector('[is_cursor]');
            if( ! cursorTarget){
                return;
            }

            let {'cursor_offset': offset, 'cursor_type': type, 'cursor_index': index, 'cursor_scroll_x': x, 'cursor_scroll_y': y} = cursorTarget.attributes
            let selection = window.getSelection();
            /*if(type.value == Node.TEXT_NODE){
                let textNode = cursorTarget.childNodes[index.value]; 
                selection.setPosition(textNode, offset.value);
            }else if(type.value == Node.ELEMENT_NODE){
                console.log(cursorTarget);
                selection.setPosition(cursorTarget, offset.value);
            }*/
            if(type.value == Node.ELEMENT_NODE){
                let node = cursorTarget.childNodes[index.value]; 
                selection.setPosition(node, offset.value);
            }else if(type.value == Node.TEXT_NODE){
                selection.setPosition(cursorTarget, offset.value);
            }
            this.#editor.scrollTo(Number(x.value), Number(y.value));
        });
    }

    undoKeyEvent(){
        if(this.#history.length == 0){
            return;
        }
        this.#historyIndex += 1;
        if(this.#historyIndex > this.#history.length - 1){
            this.#historyIndex = this.#history.length - 1
        }
        let undoRedo = this.#history[this.#historyIndex];
       
        //console.log('z key undo',undoRedo.html);
        this.#editor.innerHTML = undoRedo.html;
    }
    redoKeyEvent(){
        if(this.#history.length == 0){
            return;
        }
        this.#historyIndex -= 1;
        if(this.#historyIndex < 0){
            this.#historyIndex = 0
        }
        let undoRedo = this.#history[this.#historyIndex];

        //console.log('y key redo',undoRedo.html)
        this.#editor.innerHTML = undoRedo.html;
    }
    addUndoRedo(isCheckSkip = false){
        if(document.activeElement !== this.#editor && ! isCheckSkip){
            return;
        }

        this.rememberCursor();

        let undoRedo = new this.#UndoRedo(this.#editor.innerHTML.trim());
        this.#history.unshift(undoRedo);
        
        if(this.#history.length > this.#historyLimit){
            this.#history = this.#history.splice(0, this.#historyLimit);
        }
        
        //console.log('history', this.#history);
    }
    addUserInput(){
        let prevMils = new Date().getTime();
        this.#editor.addEventListener('keydown', (event) => {
            if(document.activeElement !== this.#editor || (new Date().getTime() - prevMils) < 500){
				return;
			}
            prevMils = new Date().getTime();
            let {ctrlKey, key, altKey} = event;
            if(ctrlKey || altKey || ctrlKey && (key == 'z' || key == 'y')){
                return;
            }
            
            if(this.#history.length != 0 && this.#history[0].html.trim() == this.#editor.innerHTML.trim()){
                return;
            }
            this.addUndoRedo();
        });
        /*
        let addElementObserver = new MutationObserver( (mutationList, observer) => {
            mutationList.forEach((mutation) => {
                let {addedNodes, removedNodes} = mutation;

                if(this.#isUndoSwitch){
                    this.#isUndoSwitch = false;
                    //return;
                }

                new Promise(resolve=>{
                    addedNodes.forEach(node=>{
                        if(node.nodeType != Node.ELEMENT_NODE){
                            return;
                        }
                        let tagName = node.tagName.toLowerCase();
                        if(this.#editor.tools.hasOwnProperty(tagName) && this.#history.length != 0 && this.#history[0].html.trim() != this.#editor.innerHTML.trim()){
                            let undoRedo = new this.#UndoRedo(this.#editor.innerHTML.trim());
                            this.#history.unshift(undoRedo);
                            if(this.#history.length > this.#historyLimit){
                                this.#history = this.#history.splice(0, this.#historyLimit);
                            }
                        }
                    });
                    resolve();
                })
            });
        })
        addElementObserver.observe(this.#editor, {
			childList:true,
			subtree: true
		});
        */
    }

    clearHistory(){
        this.#history = [];
    }
    
    clearEditor(){
        this.#editor.innerHTML = '';
    }

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Strong.js


class Strong extends FreedomInterface {

	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-strong-style'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-strong';

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'B',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Bold'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
				this.toolHandler.toolButton.dataset.tool_status = 'active';
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}
			.${this.toolHandler.defaultClass} {
				font-weight: bold;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset, {isDefaultStyle = true} = {}){
		super(Strong, dataset);
	}
	
}

;// CONCATENATED MODULE: ./view/js/handler/editor/component/Palette.js

class Palette {
	
	#style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-palette'
	});

	#palette = Object.assign(document.createElement('div'),{
		className: 'palette-wrap'
	});

	#paletteVw = 20; 
	#componentMap = undefined;
	
	#lastPanelPosition = undefined;
	
	#lastPaintPosition = undefined;
	#lastPaintRgb = [255, 0, 0];

	#lastBrightnessPosition = undefined;

	#r = 255;
	#g = 0;
	#b = 0;
	#a = 1;

    #openPositionMode;
	#openPosition;
	#exampleMode;

    #applyCallback = () => {}

	static ExampleMode = class ExampleMode{
		static #ExampleModeEnum = class ExampleModeEnum{
			value;
			constructor(value){
				this.value = value;
				Object.freeze(this);
			}
		}
		static TEXT_COLOR = new this.#ExampleModeEnum('color');
		static TEXT_BACKGROUND_COLOR = new this.#ExampleModeEnum('background-color');
		static TEXT_UNDERLINE = new this.#ExampleModeEnum('text-decoration');
		static TEXT_LINE_THROUGH = new this.#ExampleModeEnum('text-decoration');
		value;
		static{
			Object.freeze(this);
		}
		constructor(value){
			this.value = value;
			Object.freeze(this);
		}
	}

	static OpenPositionMode = class OpenPositionMode{
		static BUTTON = new OpenPositionMode('button')
		static WRAPPER = new OpenPositionMode('wrapper')
		/**
		 * @returns {String}
		 */
		value;
		static{
			Object.freeze(this);
		}
		constructor(value){
			this.value = value;
			Object.freeze(this);
		}
	}

    constructor({
		openPositionMode = Palette.OpenPositionMode.BUTTON,
		openPosition,
		exampleMode = Palette.ExampleMode.TEXT_COLOR
	}={}){
		
        this.#openPositionMode = openPositionMode;
		if( ! this.#openPositionMode ){//|| ! (this.#openPositionMode instanceof Palette.OpenPositionMode)){
			throw new Error('this is not OpenPositionMode');
		}
		this.#openPosition = openPosition;
        if( ! this.#openPosition || ! this.#openPosition.nodeType || this.#openPosition.nodeType != Node.ELEMENT_NODE){
            throw new Error('openPosition is not element');
        }
		this.#exampleMode = exampleMode;
		
		if(this.#openPositionMode == Palette.OpenPositionMode.BUTTON){
			this.#palette.style.position = "fixed";
		}

        let style = document.querySelector(`#${this.#style.id}`);
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }

        document.addEventListener("scroll", () => {
			if(this.#palette.isConnected){
				this.#processingPalettePosition(this.#palette);
			}
		});

        window.addEventListener('resize', (event) => {
            if(this.#palette.isConnected){
                this.reset();
                this.#palette.replaceChildren();
                let done = setTimeout(()=>{
                    this.open();
                    clearTimeout(done);
                    done = undefined
                },200)
            }
		})

        new Array('mouseup', 'touchend').forEach(eventName => {
			window.addEventListener(eventName, () => {
				if( ! this.#componentMap){
					return;
				}
				this.#componentMap.colorPanel?.removeAttribute('data-is_mouse_down');
				this.#componentMap.colorPaint?.removeAttribute('data-is_mouse_down');
				this.#componentMap.colorBrightness?.removeAttribute('data-is_mouse_down');
			})
		})
		
		new Array('mousemove', 'touchmove').forEach(eventName=>{
			window.addEventListener(eventName, (event) => {
				if( ! this.#componentMap){
					return;
				}
				
				if(this.#componentMap.colorPanel && this.#componentMap.colorPanel.hasAttribute('data-is_mouse_down')){
					this.#colorPanelMoveEvent(event)
				}
				if(this.#componentMap.colorPaint && this.#componentMap.colorPaint.hasAttribute('data-is_mouse_down')){
					this.#colorPaintMoveEvent(event);
				}
				if(this.#componentMap.colorBrightness && this.#componentMap.colorBrightness.hasAttribute('data-is_mouse_down')){
					this.#colorBrightnessMoveEvent(event);
				}
			})
		})
    }

    #createPalette(palette, itemMap){
		let {
			topTextWrap, selectionRgbBg, previousRgbBg, 
			colorWrap, colorPanel, colorPaint, 
			colorBrightnessWrap, colorBrightness,
			bottomTextWrap, selectionRgbText, previousRgbText,
			applyWrap, cancelButton, applyButton
		} = itemMap
		palette.replaceChildren(topTextWrap, colorWrap, colorBrightnessWrap, bottomTextWrap, applyWrap);
		this.#settingCanvas();
		this.#settingApplyEvent(palette, cancelButton, applyButton, selectionRgbText);
	}

    #createPaletteItems(){

		// 팔레트 상단 텍스트 영역
		let {topTextWrap, selectionRgbBg, previousRgbBg} = this.#createRgbaTopTextWrap()
		
		// 팔레트 중단 컬러 설정 집합 영역
		let colorWrap = Object.assign(document.createElement('div'),{
			className: 'color-wrap'
		})

		let colorPanel = this.#createColorPanel();
		let colorPaint = this.#createColorPaint();
		colorWrap.append(colorPanel, colorPanel.__colorPanelSelected, colorPanel.__colorPanelSelectedPointer,
			colorPaint, colorPaint.__colorPaintSelectedPointer);

		// 팔레트 색상 명도 조절 설정 캔버스 영역
		let {colorBrightnessWrap, colorBrightness} = this.#createColorBrightness()

		// 팔레트 하단 텍스트 영역
		let {bottomTextWrap, selectionRgbText, previousRgbText} = this.#createRgbaBottomTextWrap();
		
		let {applyWrap, cancelButton, applyButton} = this.#createApplyButtonWrap();

		return {
			topTextWrap, selectionRgbBg, previousRgbBg, 
			colorWrap, colorPanel, colorPaint, 
			colorBrightnessWrap, colorBrightness,
			bottomTextWrap, selectionRgbText, previousRgbText,
			applyWrap, cancelButton, applyButton
		};
	}

    #createColorPanel(){
		let colorPanel = Object.assign(document.createElement('canvas'),{
			className: 'palette-panel'
		})
		let colorPanelSelected = Object.assign(document.createElement('canvas'),{
			className: 'panel-selected'
		})
		let colorPanelSelectedPointer = Object.assign(document.createElement('div'),{
			className: 'panel-selected-pointer'
		});

		colorPanel.__colorPanelSelected = colorPanelSelected;
		colorPanel.__colorPanelSelectedPointer = colorPanelSelectedPointer;

		return colorPanel;
	}

    #createColorPaint(){
		let colorPaint = Object.assign(document.createElement('canvas'),{
			className: 'palette-paint'
		});

		let colorPaintSelectedPointer = Object.assign(document.createElement('div'), {
			className: 'paint-selected-pointer'
		})
		colorPaintSelectedPointer.append(Object.assign(document.createElement('div'),{
			className: 'paint-selected-pointer-child'
		}));
		colorPaint.__colorPaintSelectedPointer = colorPaintSelectedPointer

		return colorPaint;
	}

    #createColorBrightness(){
		let colorBrightnessWrap = Object.assign(document.createElement('div'), {
			className: 'brightness-wrap'
		});
		let colorBrightness = Object.assign(document.createElement('canvas'), {
			className: 'brightness-color'
		})
		let colorBrightnessSelectedPointer = Object.assign(document.createElement('div'), {
			className: 'brightness-selected-pointer'
		});
		colorBrightnessSelectedPointer.append(Object.assign(document.createElement('div'), {
			className: 'brightness-selected-pointer-child'
		}));
		colorBrightness.__colorBrightnessSelectedPointer = colorBrightnessSelectedPointer;
		colorBrightnessWrap.append(colorBrightness, colorBrightness.__colorBrightnessSelectedPointer)

		return {colorBrightnessWrap, colorBrightness};
	}

    #createRgbaTopTextWrap(){
		let topTextWrap = Object.assign(document.createElement('div'), {
			className: 'top-text-wrap'
		});
		
		let blackOrWhite = this.#blackOrWhite(this.#r, this.#g, this.#b);

		let selectionRgbBg = Object.assign(document.createElement('div'), {
			className: 'selection-rgb-bg',
			textContent: this.selectedColor
		});
		selectionRgbBg.style.background = this.selectedColor;
		selectionRgbBg.style.color = `rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`


		let previousRgbBg = Object.assign(document.createElement('div'), {
			className : 'previous-rgb-bg',
		});
		previousRgbBg.style.background = this.selectedColor;

		topTextWrap.append(selectionRgbBg, previousRgbBg)

		return {topTextWrap, selectionRgbBg, previousRgbBg}
	}

    #createRgbaBottomTextWrap(){
		let bottomTextWrap = Object.assign(document.createElement('div'), {
			className: 'bottom-text-wrap'
		});
		let selection = window.getSelection();
		let sampleText = '';
		if(selection.rangeCount != 0 && selection.isCollapsed == false){
			let range = selection.getRangeAt(0)
			let aticle = document.createElement('aticle');
			let rangeClone = range.cloneContents();
			aticle.append(rangeClone);
			sampleText = aticle
		}
		sampleText = sampleText == '' ? '가 나다 라 A BC D' : sampleText;
		let blackOrWhite = this.#blackOrWhite(this.#r, this.#g, this.#b);
		
		let selectionRgbText = Object.assign(document.createElement('div'), {
			className: 'selection-rgb-text',
		});
		this.#applyExampleTextColor(selectionRgbText, this.selectedColor, blackOrWhite);

		let previousRgbText = Object.assign(document.createElement('div'), {
			className: 'previous-rgb-text',
		});
		this.#applyExampleTextColor(previousRgbText, this.selectedColor, blackOrWhite);

		if(sampleText.nodeType && sampleText.nodeType == Node.ELEMENT_NODE){
			selectionRgbText.innerHTML = sampleText.innerHTML;
			previousRgbText.innerHTML = sampleText.innerHTML;
		}else{
			selectionRgbText.textContent = sampleText;
			previousRgbText.textContent = sampleText;
		}


		bottomTextWrap.append(selectionRgbText, previousRgbText)

		return {bottomTextWrap, selectionRgbText, previousRgbText}
	}

    #createApplyButtonWrap(){
		let applyWrap = Object.assign(document.createElement('div'),{
			className: 'button-wrap'
		})
		let cancelButton = Object.assign(document.createElement('button'), {
			className: 'cancel-button',
			type: 'button',
			textContent: 'cancel'
		})
		let applyButton = Object.assign(document.createElement('button'), {
			className: 'apply-button',
			type: 'button',
			textContent: 'apply'
		})
		applyWrap.append(cancelButton, applyButton);
		return {applyWrap, cancelButton, applyButton};
	}

    #settingCanvas(){
		return new Promise(resolve=> {
			this.#settingColorPanel()
			this.#settingColorPaint()
			this.#settingColorBrightness();
			resolve();
		})
	}

    #settingColorPanel(){
		return new Promise(resolve=>{
			let {
				topTextWrap, selectionRgbBg, previousRgbBg, 
				colorWrap, colorPanel, colorPaint, 
				colorBrightnessWrap, colorBrightness,
				bottomTextWrap, selectionRgbText, previousRgbText
			} = this.#componentMap;

			const setPanelSelectePosition = () => {
				let colorPanelRect = colorPanel.getBoundingClientRect();
				let colorPanelSelected = colorPanel.__colorPanelSelected;
				colorPanelSelected.style.top = colorPanelRect.y + 'px';
				colorPanelSelected.style.left = colorPanelRect.x + 'px';
				colorPanelSelected.style.width = colorPanelRect.width + 'px';
				colorPanelSelected.style.height = colorPanelRect.height + 'px';
				colorPanelSelected.width = colorPanelRect.width;
				colorPanelSelected.height = colorPanelRect.height;
			}

			colorPanel.onmousedown = (event)=>{
				colorPanel.setAttribute('data-is_mouse_down', '');
				let {x, y} = this.#getEventXY(event);
				colorPanel.__colorPanelSelectedPointer.style.top = y
				colorPanel.__colorPanelSelectedPointer.style.left = x
				setPanelSelectePosition()
				this.#colorPanelMoveEvent(event);
			}
			colorPanel.ontouchstart = colorPanel.onmousedown;

			colorPanel.__colorPanelSelected.onmousedown = colorPanel.onmousedown;
			colorPanel.__colorPanelSelected.ontouchstart = colorPanel.onmousedown;
			
			colorPanel.__colorPanelSelectedPointer.onmousedown = colorPanel.onmousedown;
			colorPanel.__colorPanelSelectedPointer.ontouchstart = colorPanel.onmousedown;

			resolve(setTimeout(()=>{
				let panelContext = colorPanel.getContext('2d', { willReadFrequently: true });
				let colorPanelRect = colorPanel.getBoundingClientRect();
				colorPanel.width = colorPanelRect.width;
				colorPanel.height = colorPanelRect.height;
                setPanelSelectePosition();
				this.#changeColorPanel(panelContext)

				let blackOrWhite = this.#blackOrWhite(this.#r, this.#g, this.#b);
				let context = colorPanel.__colorPanelSelected.getContext('2d', { willReadFrequently: true })
				if(this.#lastPanelPosition){
					this.#processingColorPanelSeleter(context, this.#lastPanelPosition.x - colorPanelRect.x, this.#lastPanelPosition.y - colorPanelRect.y, blackOrWhite);
				}else{
					this.#processingColorPanelSeleter(context, colorPanelRect.width - 1, 0.1, blackOrWhite);
					this.#lastPanelPosition = {x:colorPanelRect.right - 1, y:colorPanelRect.top}
                    colorPanel.__colorPanelSelectedPointer.style.top = this.#lastPanelPosition.y + 'px';
                    colorPanel.__colorPanelSelectedPointer.style.left = this.#lastPanelPosition.x + 'px';
                }
			},200))
		});
	}

    #changeColorPanel(context){
		context.clearRect(0, 0, context.canvas.width, context.canvas.height);
		let [r,g,b] = this.#lastPaintRgb
		// 가로 그라데이션
		let gradientH = context.createLinearGradient(2, 0, context.canvas.width - 2, 0);
		gradientH.addColorStop(0, 'white');
		gradientH.addColorStop(1, `rgba(${r}, ${g}, ${b}, ${this.#a})`);
		context.fillStyle = gradientH;
		context.fillRect(0, 0, context.canvas.width, context.canvas.height);
		// 수직 그라데이션
		let gradientV = context.createLinearGradient(0, 2, 0, context.canvas.height - 2);
		gradientV.addColorStop(0, 'rgba(0,0,0,0)');
		gradientV.addColorStop(1, 'black');
		context.fillStyle = gradientV;
		context.fillRect(0, 0, context.canvas.width, context.canvas.height);
	}

    #processingColorPanelSeleter(colorPanelSelectedContext, x, y, [r = 0, g = 0, b = 0] = []){
		new Promise(resolve=>{
			colorPanelSelectedContext.clearRect(0, 0, colorPanelSelectedContext.canvas.width, colorPanelSelectedContext.canvas.height);
			colorPanelSelectedContext.lineWidth = 1;
			colorPanelSelectedContext.beginPath();
			colorPanelSelectedContext.arc(x, y, 10, 0, 2 * Math.PI);
			colorPanelSelectedContext.strokeStyle = `rgb(${r}, ${g}, ${b})`
			colorPanelSelectedContext.stroke();
			resolve();
		});
	}

    #colorPanelMoveEvent(event){
		new Promise(resolve => {
			let {
				topTextWrap, selectionRgbBg, previousRgbBg, 
				colorWrap, colorPanel, colorPaint, 
				colorBrightnessWrap, colorBrightness,
				bottomTextWrap, selectionRgbText, previousRgbText
			} = this.#componentMap;
			let rect = colorPanel.getBoundingClientRect();
			let {x:pageX, y:pageY} = this.#getEventXY(event);

			let isLeftOver = pageX < rect.left;
			let isRightOver = pageX > rect.right - 1;
			let isTopOver = pageY < rect.top;
			let isBottomOver = pageY > rect.bottom;

			colorPanel.__colorPanelSelectedPointer.style.left = pageX + 'px';
			colorPanel.__colorPanelSelectedPointer.style.top = pageY + 'px';

			let {x : reProcessingRectX, y : reProcessingRectY} = colorPanel.__colorPanelSelectedPointer.getBoundingClientRect();
			
			let x = reProcessingRectX - rect.x
			let y = reProcessingRectY - rect.y


			if(isLeftOver){
				x = 0
				reProcessingRectX = rect.left
			}
			if(isRightOver){
				x = rect.width - 1
				reProcessingRectX = rect.right - 1
			}
			if(isTopOver){
				y = 0
				reProcessingRectY = rect.top
			}
			if(isBottomOver){
				y = rect.height
				reProcessingRectY = rect.bottom
			}

			this.#lastPanelPosition = {x : reProcessingRectX, y : reProcessingRectY};
			let context = colorPanel.getContext('2d', { willReadFrequently: true });
			
			/*if(isLeftOver && isTopOver){
				[this.#r, this.#g, this.#b] = [255,255,255];
			//}else if(isRightOver && isTopOver){
			//	[this.#r, this.#g, this.#b] = this.#lastPaintRgb
			}else if((isRightOver && isBottomOver) || (isLeftOver && isBottomOver) || isBottomOver){
				[this.#r, this.#g, this.#b] = [0,0,0];
			}else{*/
				[this.#r, this.#g, this.#b] = context.getImageData(x, y, 1, 1).data;
			//}

			let selectedColor = `rgba(${this.#r}, ${this.#g}, ${this.#b}, ${this.#a})`;
			let blackOrWhite = this.#blackOrWhite(this.#r,this.#g,this.#b);

			this.#processingColorPanelSeleter(
				colorPanel.__colorPanelSelected.getContext('2d', { willReadFrequently: true })
				, x, y, blackOrWhite);

			selectionRgbBg.textContent = selectedColor;
			selectionRgbBg.style.color = `rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`
			selectionRgbBg.style.background = selectedColor;
			
			this.#applyExampleTextColor(selectionRgbText, selectedColor, blackOrWhite);

			if(this.#a < 0.75){
				colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = 'solid 1px white';
			}else{
				colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = `solid 1px rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`;
			}

			this.#changeColorBrightness(colorBrightness.getContext('2d', { willReadFrequently: true }), [this.#r, this.#g, this.#b]);

			resolve();
		})
	}

    #settingColorPaint(){
		return new Promise(resolve => {
			let {
				topTextWrap, selectionRgbBg, previousRgbBg, 
				colorWrap, colorPanel, colorPaint, 
				colorBrightnessWrap, colorBrightness,
				bottomTextWrap, selectionRgbText, previousRgbText
			} = this.#componentMap;

			colorPaint.onmousedown = (event) => {
				colorPaint.setAttribute('data-is_mouse_down', '');
				let {x, y} = this.#getEventXY(event);
				colorPaint.__colorPaintSelectedPointer.style.top = y + 'px';
				this.#colorPaintMoveEvent(event);
			}
			colorPaint.ontouchstart = colorPaint.onmousedown;

			colorPaint.__colorPaintSelectedPointer.onmousedown = colorPaint.onmousedown;
			colorPaint.__colorPaintSelectedPointer.ontouchstart = colorPaint.ontouchstart;
			
			resolve(setTimeout(()=>{
				let colorPaintRect = colorPaint.getBoundingClientRect();
				colorPaint.width = colorPaintRect.width;
				colorPaint.height = colorPaintRect.height;
	
				let context = colorPaint.getContext('2d', { willReadFrequently: true } );
                context.clearRect(0, 0, colorPaint.width, colorPaint.height);

                let gradient = context.createLinearGradient(0, 2, 0, colorPaint.height - 2); 
	
				gradient.addColorStop(0, 'rgb(255, 0, 0)') // red
				gradient.addColorStop(0.15, 'rgb(255, 0, 255)') // violet
				gradient.addColorStop(0.35, 'rgb(0, 0, 255)') // blue
				gradient.addColorStop(0.45, 'rgb(0, 255, 255)') // Sky blue
				gradient.addColorStop(0.65, 'rgb(0, 255, 0)') // green
				gradient.addColorStop(0.85, 'rgb(255, 255, 0)') // yellow
				gradient.addColorStop(0.9, 'orange')
				gradient.addColorStop(1, 'rgb(255, 0, 0)') // red
				context.fillStyle = gradient;
				context.fillRect(0, 0, colorPaint.width, colorPaint.height);

				if(this.#lastPaintPosition){
					colorPaint.__colorPaintSelectedPointer.style.top = this.#lastPaintPosition.y + 'px';
				}else{
					colorPaint.__colorPaintSelectedPointer.style.top = colorPaintRect.y + 'px';
					this.#lastPaintPosition = colorPaintRect.y;
				}
				colorPaint.__colorPaintSelectedPointer.style.left = colorPaintRect.x + 'px';
				colorPaint.__colorPaintSelectedPointer.style.width = colorPaintRect.width + 'px';
				let [r,g,b] = this.#blackOrWhite(...this.#lastPaintRgb);
				colorPaint.__colorPaintSelectedPointer.children[0].style.border = `solid 1px rgb(${r}, ${g}, ${b})`
			},200));
		});
	}

    #colorPaintMoveEvent(event){
		new Promise(resolve => {
			let {
				topTextWrap, selectionRgbBg, previousRgbBg, 
				colorWrap, colorPanel, colorPaint, 
				colorBrightnessWrap, colorBrightness,
				bottomTextWrap, selectionRgbText, previousRgbText
			} = this.#componentMap;
			let rect = colorPaint.getBoundingClientRect();
			let pageY = this.#getEventXY(event).y;

			let isTopOver = pageY < rect.top;
			let isBottomOver = pageY > rect.bottom - 1;

			colorPaint.__colorPaintSelectedPointer.style.top = pageY + 'px';

			let {y : reProcessingRectY} = colorPaint.__colorPaintSelectedPointer.getBoundingClientRect();
			
			let y = reProcessingRectY - rect.y


			let isOver = false;
			if(isTopOver){
				y = 0;
				reProcessingRectY = rect.top;
				isOver = true;
			}
			if(isBottomOver){
				y = rect.height - 1
				reProcessingRectY = rect.bottom - 1
				isOver = true;
			}

			this.#lastPaintPosition = {y: reProcessingRectY}
			let context = colorPaint.getContext('2d', { willReadFrequently: true });
			let r,g,b;
			if(isOver){
				[r,g,b] = [255, 0, 0];
			}else{
				[r,g,b] = context.getImageData(1, y, 1, 1).data;
			}
			this.#lastPaintRgb = [r,g,b]

			colorPaint.__colorPaintSelectedPointer.style.top = reProcessingRectY + 'px';

			let blackOrWhite = this.#blackOrWhite(r,g,b);

			colorPaint.__colorPaintSelectedPointer.children[0].style.border = `solid 1px rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`

			if(this.#a < 0.75){
				colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = 'solid 1px white';
			}else{
				colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = `solid 1px rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`;
			}

			

			this.#changeColorBrightness(colorBrightness.getContext('2d', { willReadFrequently: true }), [r,g,b]);

			this.#changeColorPanel(colorPanel.getContext('2d', { willReadFrequently: true }));
			let colorPanelRect = colorPanel.getBoundingClientRect();
			this.#processingColorPanelSeleter(
				colorPanel.__colorPanelSelected.getContext('2d', { willReadFrequently: true }),
				this.#lastPanelPosition.x - colorPanelRect.x,
				this.#lastPanelPosition.y - colorPanelRect.y,
			)
			this.#colorPanelMoveEvent({x:this.#lastPanelPosition.x, y:this.#lastPanelPosition.y})
			resolve();
		})
	}

    #settingColorBrightness(){
		new Promise(resolve => {
			let {
				topTextWrap, selectionRgbBg, previousRgbBg, 
				colorWrap, colorPanel, colorPaint, 
				colorBrightnessWrap, colorBrightness,
				bottomTextWrap, selectionRgbText, previousRgbText
			} = this.#componentMap;

			colorBrightness.onmousedown = (event) => {
				colorBrightness.setAttribute('data-is_mouse_down', '');
				let {x, y} = this.#getEventXY(event);
				colorBrightness.__colorBrightnessSelectedPointer.style.left = x + 'px';
				this.#colorBrightnessMoveEvent(event);
			}
			colorBrightness.ontouchstart = colorBrightness.onmousedown;

			colorBrightness.__colorBrightnessSelectedPointer.onmousedown = colorBrightness.onmousedown
			colorBrightness.__colorBrightnessSelectedPointer.ontouchstart = colorBrightness.onmousedown
			
			setTimeout(() => {
                let blackOrWhite = this.#blackOrWhite(this.#r, this.#g, this.#b);
				let colorBrightnessRect = colorBrightness.getBoundingClientRect()
				colorBrightness.width = colorBrightnessRect.width;
				colorBrightness.height = colorBrightnessRect.height;
	
				let context = colorBrightness.getContext('2d', { willReadFrequently: true });
				this.#changeColorBrightness(context, [this.#r, this.#g, this.#b]);

				if(this.#lastBrightnessPosition){
					colorBrightness.__colorBrightnessSelectedPointer.style.left = this.#lastBrightnessPosition.x + 'px';
				}else{
					colorBrightness.__colorBrightnessSelectedPointer.style.left = colorBrightnessRect.right + 'px';
				}
				
				colorBrightness.__colorBrightnessSelectedPointer.style.top = colorBrightnessRect.y + 'px';
				colorBrightness.__colorBrightnessSelectedPointer.style.height = colorBrightnessRect.height + 'px';
                colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = `solid 1px rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`
            }, 200)
			
			resolve();
		})
	}

    #changeColorBrightness(context, [r = 255, g=0, b=0]){
		context.clearRect(0, 0, context.canvas.width, context.canvas.height);
		let gradient = context.createLinearGradient(0, 0, context.canvas.width, 0) 
		gradient.addColorStop(0, 'rgba(0,0,0,0)');
		gradient.addColorStop(1, `rgba(${r}, ${g}, ${b}, ${this.#a})`); // 페인트 컬러로 변경 필요
		context.fillStyle = gradient;
		context.fillRect(0, 0, context.canvas.width, context.canvas.height);
	}

    #colorBrightnessMoveEvent(event){
		new Promise(resolve=> {
			let {
				topTextWrap, selectionRgbBg, previousRgbBg, 
				colorWrap, colorPanel, colorPaint, 
				colorBrightnessWrap, colorBrightness,
				bottomTextWrap, selectionRgbText, previousRgbText
			} = this.#componentMap;
			let rect = colorBrightness.getBoundingClientRect();

			let {x:pageX, y:pageY} = this.#getEventXY(event);

			let isLeftOver = pageX < rect.left;
			let isRightOver = pageX > rect.right;

			colorBrightness.__colorBrightnessSelectedPointer.style.left = pageX + 'px';

			let {x : reProcessingRectX} = colorBrightness.__colorBrightnessSelectedPointer.getBoundingClientRect();
			
			let x = rect.x - reProcessingRectX

			if(isLeftOver){
				x = rect.x
				reProcessingRectX = rect.x
				colorBrightness.__colorBrightnessSelectedPointer.style.left = rect.x + 'px'
				this.#a = 0
			}else if(isRightOver){
				x = rect.right
				reProcessingRectX = rect.right
				colorBrightness.__colorBrightnessSelectedPointer.style.left = rect.right + 'px';
				this.#a = 1
			}else{
				this.#a = ((reProcessingRectX - rect.x) / rect.width).toFixed(2)
			}

			let blackOrWhite = this.#blackOrWhite(this.#r, this.#g, this.#b);
			
			selectionRgbBg.textContent = this.selectedColor;
			selectionRgbBg.style.background = this.selectedColor;

			selectionRgbText.style.color = this.selectedColor;
			this.#applyExampleTextColor(selectionRgbText, this.selectedColor, blackOrWhite);
			if(this.#a < 0.75){
				colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = 'solid 1px white';
			}else{
				colorBrightness.__colorBrightnessSelectedPointer.children[0].style.border = `solid 1px rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`;
			}

			resolve()
		});
	}

    #settingApplyEvent(palette, cancelButton, applyButton, selectionRgbText){
		cancelButton.onclick = () => {
			this.close();
			//palette.remove();
		}
		applyButton.onclick = (event) => {
            this.applyCallback(event);
		}
	}

    #processingPalettePosition(palette){
		if(this.#openPositionMode == Palette.OpenPositionMode.BUTTON){
			let {x, y, height} = this.#openPosition.getBoundingClientRect();
			//let paletteWidthPx = document.documentElement.clientHeight * (this.#paletteVw / 100);
			//let paletteHeightPx = document.documentElement.clientHeight * (this.#paletteVh / 100);
			let paletteHeightPx = palette.clientHeight;
			let paletteTop = (y - paletteHeightPx)
			if(paletteTop > 0){
				palette.style.top = paletteTop + 'px';
			}else{
				palette.style.top = y + height + 'px';
			}
			palette.style.left = x + 'px';
		}else if(this.#openPositionMode == Palette.OpenPositionMode.WRAPPER){
			this.#openPosition.append(palette);
		}
	}

	/**
	 * @see https://stackoverflow.com/a/3943023
	 * @param  {...any} rgb 
	 * @returns 
	 */
    #blackOrWhite(...rgb){
		let [r,g,b] = rgb;
		return (r * 0.299 + g * 0.587 + b * 0.114) > 186
            ? [0, 0, 0]
            : [255, 255, 255];
	}

    #getEventXY(event){
		let x;
		let y;
		if(window.TouchEvent && event.constructor == window.TouchEvent){
			x = event.touches[0].pageX
			y = event.touches[0].pageY
			event.preventDefault();
		}else{
			x = event.x
			y = event.y
		}
		return {x,y};
	}

	/**
	 * 
	 * @param {HTMLElement} text 
	 * @param {String} color 
	 * @param {Array<Number>} blackOrWhite 
	 */
	#applyExampleTextColor(text, color, blackOrWhite){
		if(this.#exampleMode == Palette.ExampleMode.TEXT_COLOR){
			text.style.color = color
			if(blackOrWhite && blackOrWhite.length != 0){
				text.style.backgroundColor = `rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`
			}
		}else if(this.#exampleMode == Palette.ExampleMode.TEXT_BACKGROUND_COLOR){
			text.style.backgroundColor = color
			if(blackOrWhite && blackOrWhite.length != 0){
				text.style.color = `rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`
			}
		}else if(this.#exampleMode == Palette.ExampleMode.TEXT_UNDERLINE){
			text.style.textDecoration = 'underline'
			text.style.textDecorationColor = color
			
			if(blackOrWhite && blackOrWhite.length != 0){
				text.style.color = `rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`
				text.style.backgroundColor = 'rgba(255, 255, 255, 0.25)'
				/*if(blackOrWhite.filter(e=>e==255).length == 3){
					text.style.backgroundColor = 'rgba(0, 0, 0, 0.3)'
				}else{
					text.style.backgroundColor = 'rgba(255, 255, 255, 0.25)'
				}*/
			}
			
		}else if(this.#exampleMode == Palette.ExampleMode.TEXT_LINE_THROUGH){
			text.style.textDecoration = 'line-through'
			text.style.textDecorationColor = color;
			
			if(blackOrWhite && blackOrWhite.length != 0){
				text.style.color = `rgb(${blackOrWhite[0]}, ${blackOrWhite[1]}, ${blackOrWhite[2]})`
				text.style.backgroundColor = 'rgba(255, 255, 255, 0.25)'
			}
			
		}
	}

    get selectedColor(){
		return `rgba(${this.#r}, ${this.#g}, ${this.#b}, ${this.#a})`;
	}

    get palette(){
        return this.#palette;
    }

    get isConnected(){
        return this.#palette.isConnected;
    }

    set applyCallback(applyCallback){
        this.#applyCallback = applyCallback;
    }

    get applyCallback(){
        return this.#applyCallback;
    }

	get style(){
		return this.#style;
	}

	set style(style){
        this.#style.textContent = style;
    }

	set insertStyle(style){
		this.#style.sheet.insertRule(style);
	}

	get r(){
		return this.#r;
	}
	get g(){
		return this.#g;
	}
	get b(){
		return this.#b;
	}
	get a(){
		return this.#a;
	}

    reset(){
		this.#r = 255;
		this.#g = 0;
		this.#b = 0;
		this.#a = 1;
		this.#lastPaintRgb = [255, 0, 0];
		this.#lastPanelPosition = undefined;
		this.#lastPaintPosition = undefined;
		this.#lastBrightnessPosition = undefined;
        this.#componentMap = undefined;
        
	}

    close(){
        this.#palette.remove();
    }

    open(){
        this.#componentMap = this.#createPaletteItems();
        document.body.append(this.#palette);
        this.#createPalette(this.#palette, this.#componentMap);
        this.#processingPalettePosition(this.#palette);
    }

    createStyle(){
		//position: fixed; 제거 20230517
		this.#style.textContent = `
			.palette-wrap{
				background: #343434;
				padding: 0.9%;
				width: ${this.#paletteVw}vw;
				height: fit-content;
				color: white;
				font-size: 13px;
				min-width: 300px;
				z-index: 999;
				-webkit-user-select:none;
				-moz-user-select:none;
				-ms-user-select:none;
				user-select:none
			}
			.palette-wrap .palette-panel{
				width: 90%;
				position: relative;
			}

			/* 상단 텍스트 영역 [S] */
			.palette-wrap .top-text-wrap{
				display: flex;
				justify-content: space-between;
				margin-bottom: 2%;
				height: 8%;
			}
			.palette-wrap .top-text-wrap .selection-rgb-bg{
				width: 100%;
				text-align-last: center;
				display: flex;
				justify-content: center;
				align-items: center;
			}
			.palette-wrap .top-text-wrap .previous-rgb-bg{
				text-align-last: center;
				display: flex;
				justify-content: center;
				align-items: center;
				width: 11.5%;
			}
			/* 상단 텍스트 영역 [E] */

			/* 컬러 영역 영역 [S] */
			.palette-wrap .color-wrap{
				display: flex;
				justify-content: space-between;
				margin-bottom: 2%;
			}

			.palette-wrap .color-wrap .panel-selected{
				position: fixed;
				z-index: 9999;
			}
			.palette-wrap .color-wrap .panel-selected-pointer{
				position: fixed;
				width: 1px;
				height: 1px;
			}

			.palette-wrap .color-wrap .palette-paint{
				width: 5%;
			}
			.palette-wrap .color-wrap .paint-selected-pointer{
				position: fixed;
				height: 1px;
				display: flex;
				align-items: center;
				justify-content: center;
				background-color: #faebd700;
			}
			.palette-wrap .color-wrap .paint-selected-pointer-child{
				position: absolute;
				border: solid 1px;
				height: 2px;
				width: inherit;
			}
			/* 컬러 영역 영역 [E] */

			/* 투명도 영역 [S] */
			.palette-wrap .brightness-wrap{
				margin-bottom: 2%;
				background-image: /* tint image */ linear-gradient(to right, rgb(192 192 192 / 20%), rgb(192 192 192 / 20%)), /* checkered effect */ linear-gradient(to right, #505050 50%, #a1a1a1 50%), linear-gradient(to bottom, #505050 50%, #a1a1a1 50%);
				background-blend-mode: normal, difference, normal;
				background-size: 2em 2em;
				display: flex;
			}
			.palette-wrap .brightness-wrap .brightness-color{
				height: 2vh;
				width: 100%;
				min-height: 17px;
			}
			.palette-wrap .brightness-wrap .brightness-selected-pointer{
				position: fixed;
				width: 1px;
				display: flex;
				align-items: center;
				justify-content: center;
				background-color: #faebd700;
			}
			.palette-wrap .brightness-wrap .brightness-selected-pointer-child{
				position: absolute;
				border: solid 1px;
				height: inherit;
				width: 2px;
				background: inherit;
			}
			/* 투명도 영역 [E] */

			/* 하단 텍스트 영역 [S] */
			.palette-wrap .bottom-text-wrap{
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 2%;
			}
			.palette-wrap .bottom-text-wrap .selection-rgb-text{
				margin-right: 5%;
			}
			.palette-wrap .bottom-text-wrap .selection-rgb-text, .palette-wrap .bottom-text-wrap .previous-rgb-text{
				overflow-x: hidden;
				max-width: 40%;
				text-overflow: ellipsis;
				width: fit-content;
				background: rgba(255, 255, 255, 0.25);
				color: rgba(255, 255, 255, 0.25);
			}
			.palette-wrap .bottom-text-wrap .selection-rgb-text *, .palette-wrap .bottom-text-wrap .previous-rgb-text *{
				text-overflow: ellipsis;
				overflow-x: hidden;
			}
			/* 하단 텍스트 영역 [E] */

			/* 버튼 영역 [S] */
			.palette-wrap .button-wrap {
				display: flex;
				justify-content: space-around;
			}
			.palette-wrap .cancel-button, .palette-wrap .apply-button{
				background: none;
				border: revert;
				color: #b9b9b9;
				border-color: #464646;
			}
			/* 버튼 영역 [E] */
		`;
		return this.#style;
	}
}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Color.js




class Color extends FreedomInterface {
	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-color-style'
	});

	static palette;

	static{

		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-color';
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'C',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Font Color'
        });

		this.palette = new Palette({
            openPositionMode: Palette.OpenPositionMode.BUTTON, 
            openPosition : this.toolHandler.toolButton
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.palette.isConnected){
				this.palette.close();
			}else{
				this.palette.open();
			}
		}

		this.palette.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.palette.close();
		}

		super.outClickElementListener(this.palette.palette, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.palette.palette.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.palette.close();
			}
		})
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset){
		super(Color, dataset);
		if( ! dataset && Object.entries(this.dataset).length == 0){
			this.dataset.rgba = Color.palette.r + ',' + Color.palette.g + ',' + Color.palette.b + ',' + Color.palette.a;
		}
		this.style.color = `rgba(${this.dataset.rgba})`;
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Background.js




class Background extends FreedomInterface {
	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-background-style'
	});

	static palette;

	static{

		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-background';
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'G',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Background Color'
        });

		this.palette = new Palette({
            openPositionMode: Palette.OpenPositionMode.BUTTON, 
            openPosition: this.toolHandler.toolButton,
			exampleMode: Palette.ExampleMode.TEXT_BACKGROUND_COLOR
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.palette.isConnected){
				this.palette.close();
			}else{
				this.palette.open();
			}
		}

		this.palette.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.palette.close();
		}

		super.outClickElementListener(this.palette.palette, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.palette.palette.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.palette.close();
			}
		})

	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset){
		super(Background, dataset);

		if( ! dataset && Object.entries(this.dataset).length == 0){
			this.dataset.rgba = Background.palette.r + ',' + Background.palette.g + ',' + Background.palette.b + ',' + Background.palette.a;
		}
		this.style.backgroundColor = `rgba(${this.dataset.rgba})`;
	}



}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Strikethrough.js




class Strikethrough extends FreedomInterface {

	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-strikethrough-style'
	});

    static palette;
	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-strikethrough';
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'S',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Strikethrough'
        });

		this.palette = new Palette({
            openPositionMode: Palette.OpenPositionMode.BUTTON, 
            openPosition: this.toolHandler.toolButton,
			exampleMode: Palette.ExampleMode.TEXT_LINE_THROUGH
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.palette.isConnected){
				this.palette.close();
			}else{
				this.palette.open();
			}
		}

		this.palette.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.palette.close();
		}

		super.outClickElementListener(this.palette.palette, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.palette.palette.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.palette.close();
			}
		})
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
				text-decoration: 1px line-through;
				text-decoration-color: #404040;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset){
		super(Strikethrough, dataset);
		if( ! dataset && Object.entries(this.dataset).length == 0){
			this.dataset.rgba = Strikethrough.palette.r + ',' + Strikethrough.palette.g + ',' + Strikethrough.palette.b + ',' + Strikethrough.palette.a;
		}

		this.style.textDecoration = `line-through rgba(${this.dataset.rgba}) 1px`;
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Underline.js




class Underline extends FreedomInterface {
	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-underline-style'
	});

	static palette;

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-underline';
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'U',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Underline'
        });

		this.palette = new Palette({
            openPositionMode: Palette.OpenPositionMode.BUTTON, 
            openPosition: this.toolHandler.toolButton,
			exampleMode: Palette.ExampleMode.TEXT_UNDERLINE
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.palette.isConnected){
				this.palette.close();
			}else{
				this.palette.open();
			}
		}

		this.palette.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.palette.close();
		}

		super.outClickElementListener(this.palette.palette, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.palette.palette.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.palette.close();
			}
		})
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
				text-decoration: 1px underline;
			}
			.${this.toolHandler.defaultClass} {
				width: fit-content;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset, {isDefaultStyle = true} = {}){
		super(Underline, dataset);
		if( ! dataset && Object.entries(this.dataset).length == 0){
			this.dataset.rgba = Underline.palette.r + ',' + Underline.palette.g + ',' + Underline.palette.b + ',' + Underline.palette.a;
		}
		this.style.borderBottom = `solid 2px rgba(${this.dataset.rgba})`
		//this.style.backgroundImage = `linear-gradient(to right, rgba(${this.dataset.rgba}), rgba(${this.dataset.rgba}))`
		//this.style.textDecoration = `underline rgba(${this.dataset.rgba}) 1px`;
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/component/FontFamilyBox.js
class FontFamilyBox {
    
    #style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-font-family-box'
	});

    #fontFamilyBoxVw = 20;

    #fontList = [];
    #fontFamilyBox = Object.assign(document.createElement('div'), {
        className: 'font-box-wrap',
    })
    #fontFamilyBoxContainer = Object.assign(document.createElement('ul'), {
        className: 'font-box-container'
    })
    /*
    #searchInputText = Object.assign(document.createElement('input'), {
        autocomplete: 'off',
        placeholder: 'search font',
        type: 'text',
        name: 'font-box-search',
        className: 'font-box-search'
    })
    */
    #fontElementList = [];
    #selectedFont;

    #defaultSampleText = '가나 다라 ab cd'
    #sampleText = this.#defaultSampleText;

    #applyCallback = () => {}

    constructor(fontList){
        if( ! fontList){
            throw new Error('fontList is undefined');
        }
        this.#fontList = fontList;

        let style = document.querySelector(`#${this.#style.id}`);
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }

        /*
        let searchWrap = Object.assign(document.createElement('div'),{
            className: 'font-box-search-wrap'
        });
        searchWrap.append(this.#searchInputText);
        
        this.#fontFamilyBox.append(searchWrap, this.#fontFamilyBoxContainer);
        */
        this.#fontFamilyBox.append(this.#fontFamilyBoxContainer);

    }

    searchInputTextEvent(event){
        
    }

    #addFontItemEvent(item){
        return new Promise(res=>{
            item.onclick = (event) => {
                this.#selectedFont = item;
                this.applyCallback(event);
            }
            res(item);
        })
    }

    #createFontElementList(sampleText){
        return new Promise(resolve=> {
            const createFontItem = () => {
                let li = Object.assign(document.createElement('li'),{
                    className: 'font-family-item',
                });
                if(sampleText.nodeType && sampleText.nodeType == Node.ELEMENT_NODE){
                    li.innerHTML = sampleText.innerHTML;
                }else{
                    li.textContent = sampleText;
                }
                this.#addFontItemEvent(li)
                return li;
            }
            this.#fontElementList = this.#fontList.map(fontFamily=>{
                let li = createFontItem();
                li.style.fontFamily = fontFamily;
                return li;
            });

            let defaultFont = createFontItem();
            this.#fontElementList.push(defaultFont)

            resolve(this.#fontElementList);
        })
    }

    async open(){
        let selection = window.getSelection();
        if(selection.rangeCount != 0 && selection.isCollapsed == false){
            let range = selection.getRangeAt(0)
            let aticle = document.createElement('aticle');
            let rangeClone = range.cloneContents();
            aticle.append(rangeClone);
            this.#sampleText = aticle;
        }
        this.#sampleText = this.#sampleText == '' ? this.#defaultSampleText : this.#sampleText;
    
        document.body.append(this.#fontFamilyBox);

        return await this.#createFontElementList(this.#sampleText).then(fontElementList => {
            this.#fontFamilyBoxContainer.replaceChildren(...fontElementList);
            return this.#fontFamilyBoxContainer;
        });
        
    }
    close(){
        this.#fontFamilyBox.remove();
    }

    set applyCallback(applyCallback){
        this.#applyCallback = applyCallback;
    }

    get applyCallback(){
        return this.#applyCallback;
    }

    get fontFamilyBox(){
        return this.#fontFamilyBox;
    }

    get selectedFont(){
        return this.#selectedFont;
    }

	get style(){
		return this.#style;
	}

	set style(style){
        this.#style.textContent = style;
    }

	set insertStyle(style){
		this.#style.sheet.insertRule(style);
	}


    createStyle(){
        this.#style.textContent = `
            .font-box-wrap{
                background: #000000bf;
                position: fixed;
				padding: 0.9%;
				width: ${this.#fontFamilyBoxVw}vw;
                height: 25vh;
				color: white;
				font-size: 13px;
				min-width: 300px;
                overflow-y: auto;
				-webkit-user-select:none;
				-moz-user-select:none;
				-ms-user-select:none;
				user-select:none;
                z-index: 999;
            }
            .font-box-wrap .font-box-container{
                list-style-type: none;
                padding: 0;
                margin: 0;
            }
            .font-box-wrap .font-family-item{
                margin-bottom: 1%;
            }
            .font-box-wrap .font-family-item:hover{
                background-color: #747474;
                cursor: pointer;
            }

        `
        return this.#style;
    }
}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/FontFamily.js




class FontFamily extends FreedomInterface {
    static toolHandler = new ToolHandler(this);

    static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-font-family-style'
	});

    static fontFamilyBox;

    static #fontList = [
        'Arial, Helvetica, Sans-Serif',
        'Arial Black, Gadget, Sans-Serif',
        'Comic Sans MS, Textile, Cursive',
        'Courier New, Courier, Monospace',
        'Georgia, Times New Roman, Times, Serif',
        'Impact, Charcoal, Sans-Serif',
        'Lucida Console, Monaco, Monospace',
        'Lucida Sans Unicode, Lucida Grande, Sans-Serif',
        'Palatino Linotype, Book Antiqua, Palatino, Serif',
        'Tahoma, Geneva, Sans-Serif',
        'Times New Roman, Times, Serif',
        'Trebuchet MS, Helvetica, Sans-Serif',
        'Verdana, Geneva, Sans-Serif',
        'MS Sans Serif, Geneva, Sans-Serif',
        'MS Serif, New York, Serif'
    ]
    static{
        
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-font-family';
		
        this.fontFamilyBox = new FontFamilyBox(this.#fontList);

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'F',
            className: `${this.#defaultStyle.id}-button`,
            title: 'Font Family'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.fontFamilyBox.fontFamilyBox.isConnected){
				this.fontFamilyBox.close();
			}else{
				this.fontFamilyBox.open().then(fontFamilyBoxContainer=>{
				    this.toolHandler.processingElementPosition(this.fontFamilyBox.fontFamilyBox);
                });
			}
		}

        this.fontFamilyBox.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.fontFamilyBox.close();
		}

        document.addEventListener("scroll", () => {
			if(this.fontFamilyBox.fontFamilyBox.isConnected){
				this.toolHandler.processingElementPosition(this.fontFamilyBox.fontFamilyBox);
			}
		});
        window.addEventListener('resize', (event) => {
            if(this.fontFamilyBox.fontFamilyBox.isConnected){
                this.fontFamilyBox.open().then(fontFamilyBoxContainer=>{
				    this.toolHandler.processingElementPosition(this.fontFamilyBox.fontFamilyBox);
                });
            }
		})

		super.outClickElementListener(this.fontFamilyBox.fontFamilyBox, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.fontFamilyBox.fontFamilyBox.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
                this.fontFamilyBox.close();
			}
		})
	}

    static createDefaultStyle(){
		this.#defaultStyle.textContent = `
            .${this.#defaultStyle.id}-button{
                font-size: 0.8rem;
            }
        `
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

    static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset){
		super(FontFamily, dataset);
		if( ! dataset && Object.entries(this.dataset).length == 0){
            this.dataset.font_family = FontFamily.fontFamilyBox.selectedFont?.style.fontFamily;
        }
        this.style.fontFamily = this.dataset.font_family
    }
	
}

;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Quote.js



class Quote extends FreedomInterface {
	//static extendsElement = 'strong';
	//static defaultClass = 'line';
	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-quote-style'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-quote';
		this.toolHandler.isInline = false;

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'Q',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Quote'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
				this.toolHandler.toolButton.dataset.tool_status = 'active';
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}
			.${this.toolHandler.defaultClass} {
				display: block;
				padding-left: 1em;
				border-left: 3px solid #d7d7db;
				margin-inline: 2.5em;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	parentLine;

	constructor(dataset){
		super(Quote, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});

		/*
		super.connectedAfterOnlyOneCallback = () => {
			this.dataset.index = Quote.toolHandler.connectedFriends.length;
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}else{
				nextLine.line.lookAtMe();
			}
		}

        super.disconnectedChildAfterCallback = () => {
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}
        }
		*/
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/NumericPoint.js



class NumericPoint extends FreedomInterface {
	//static extendsElement = 'strong';
	//static defaultClass = 'line';
	static toolHandler = new ToolHandler(this);
	
	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-numeric-point-style'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-numeric-point';
		this.toolHandler.isInline = false;

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '1.',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Numeric Point'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
				this.toolHandler.toolButton.dataset.tool_status = 'active';
			}
		}
	}
    //list-style-type: disc;
	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}
			.${this.toolHandler.defaultClass} {
				display: block;
				margin-inline: 1.3em;
			}
			.${this.toolHandler.defaultClass} > * {
				margin-inline: 1.3em;
			}
			.${this.toolHandler.defaultClass} > *:before{
				content: attr(data-order)
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}
	
	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	parentLine;
	
	constructor(dataset){
		super(NumericPoint, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});
		super.connectedChildAfterCallback = (list) => {
			new Promise(r=>{
				[...this.children].forEach((e,i)=>{
					e.dataset.order = (i + 1) + '. ';
				});
				r();
			});
			let lastItem = list.at(-1);
			if(lastItem.nodeType == Node.ELEMENT_NODE){
				let inter = setInterval(()=>{
					if(lastItem.isConnected){
						clearInterval(inter);
						window.getSelection().setPosition(lastItem, lastItem.childNodes.length)
					}
				}, 50);
			}else if(lastItem.nodeType == Node.TEXT_NODE){
				let inter = setInterval(()=>{
					if(lastItem.isConnected){
						clearInterval(inter);
						window.getSelection().setPosition(lastItem, lastItem.length)
					}
				}, 50);
			}
		}

		/*
		super.connectedAfterOnlyOneCallback = () => {
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}else{
				nextLine.line.lookAtMe();
			}
		}
		/*
		super.disconnectedChildAfterCallback = (removedNodes) => {
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}
        }
		*/
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/BulletPoint.js



class BulletPoint extends FreedomInterface {
	//static extendsElement = 'strong';
	//static defaultClass = 'line';
	static toolHandler = new ToolHandler(this);
	
	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-bullet-point-style'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-bullet-point';
		this.toolHandler.isInline = false;
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '●',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Bullet Point'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
				this.toolHandler.toolButton.dataset.tool_status = 'active';
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}

			.${this.toolHandler.defaultClass} {
				display: block;
				padding-left: 1em;
				margin-inline: 2.5em;
				list-style-type: disc;
			}
			.${this.toolHandler.defaultClass} > *{
				display: list-item;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}
	
	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	parentLine;

	constructor(dataset){
		super(BulletPoint, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});
		/*
		super.connectedAfterOnlyOneCallback = () => {
			this.dataset.index = BulletPoint.toolHandler.connectedFriends.length;
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}else{
				nextLine.line.lookAtMe();
			}
		}

		super.disconnectedChildAfterCallback = (removedNodes) => {
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}
        }
		*/
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/component/SortBox.js
class SortBox {
    
    #style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-sort-box'
	});

    #sortBox = Object.assign(document.createElement('div'), {
        className: 'sort-box-wrap',
    })

    #sortBoxContainer = Object.assign(document.createElement('ul'),{
        className: 'sort-box-container',
    })

    #sortBoxItems = [/*'left',*/ 'center', 'right'].map(e=>Object.assign(document.createElement('li'),{
        textContent: e,
    }));

    #selectedSort;

    #applyCallback = () => {}
    
    constructor(){
        let style = document.querySelector(`#${this.#style.id}`);
        
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }

        this.#sortBox.append(this.#sortBoxContainer);
        this.#sortBoxContainer.replaceChildren(...this.#sortBoxItems);
        this.#sortBoxItems.forEach(e=>{
            e.onclick = (event) => {
                this.#selectedSort = e; 
                this.applyCallback(event);
            }
        });
    }

    open(){
        document.body.append(this.#sortBox);
    }

    close(){
        this.#sortBox.remove();
    }

    set applyCallback(applyCallback){
        this.#applyCallback = applyCallback;
    }

    get applyCallback(){
        return this.#applyCallback;
    }

    get sortBox(){
        return this.#sortBox;
    }

    get selectedSort(){
        return this.#selectedSort;
    }

	get style(){
		return this.#style;
	}

	set style(style){
        this.#style.textContent = style;
    }

	set insertStyle(style){
		this.#style.sheet.insertRule(style);
	}
    
    
    createStyle(){
        this.#style.textContent = `
            .sort-box-wrap {
                background: #000000bf;
                position: fixed;
                padding: 0.9%;
                height: fit-content;
                color: white;
                font-size: 13px;
                min-width: 85px;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                z-index: 999;
            }
            .sort-box-wrap .sort-box-container {
                list-style-type: none;
                padding: 0;
                margin: 0;
                display: flex;
                gap: 1vw;
            }
            
            .sort-box-wrap .sort-box-container li{
                transition: all 0.5s;
                padding: 1% 2% 1% 2%;
                margin-right: 1vw;
            }
            .sort-box-wrap .sort-box-container li:hover{
                background-color: #95959591;
                cursor: pointer;
            }

        `
        return this.#style;
    }

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Sort.js




class Sort extends FreedomInterface {
    static toolHandler = new ToolHandler(this);
	
	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-index-style'
	});

    static sortBox; 

    static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-index';
		this.toolHandler.isInline = false;

        this.sortBox = new SortBox();

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'Ξ',
            className: `${this.#defaultStyle.id}-button`,
			title: 'align'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.sortBox.sortBox.isConnected){
				this.sortBox.close();
			}else{
				this.sortBox.open();
                this.toolHandler.processingElementPosition(this.sortBox.sortBox);
			}
		}

        this.sortBox.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.sortBox.close();
		}

        document.addEventListener("scroll", () => {
			if(this.sortBox.sortBox.isConnected){
				this.toolHandler.processingPalettePosition(this.sortBox.sortBox);
			}
		});
        window.addEventListener('resize', (event) => {
            if(this.sortBox.sortBox.isConnected){
                this.sortBox.open();
                this.toolHandler.processingElementPosition(this.sortBox.sortBox);
            }
		})

		super.outClickElementListener(this.sortBox.sortBox, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.sortBox.sortBox.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.sortBox.close();
			}
		})
	}
    static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}

            .${this.toolHandler.defaultClass} {
                display: block;
            }
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

    static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset){
		super(Sort, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});

		if( ! dataset && Object.entries(this.dataset).length == 0){
            this.dataset.text_align = Sort.sortBox.selectedSort?.textContent;
        }
        this.style.textAlign = this.dataset.text_align;

		super.connectedAfterOnlyOneCallback = () => {
			this.dataset.index = Sort.toolHandler.connectedFriends.length;
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}else{
				nextLine.line.lookAtMe();
			}
		}

		super.disconnectedChildAfterCallback = (removedNodes) => {
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}
        }

	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/component/FontSizeBox.js
class FontSizeBox {
    
    #style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-font-size-box'
	});

    #fontSizeBoxVw = 30;

    #fontSizeBox = Object.assign(document.createElement('div'), {
        className: 'font-size-wrap',
    })
    #fontSizeBoxContainer = Object.assign(document.createElement('ul'), {
        className: 'font-size-container'
    })
    
    #searchInputText = Object.assign(document.createElement('input'), {
        autocomplete: 'off',
        placeholder: 'search your font size',
        type: 'number',
        name: 'font-size-search',
        className: 'font-size-search',
        oninput: (event) => this.#searchInputTextEvent(event),
        onkeyup: (event) => {
            if(event.key == 'Enter' && this.#searchInputText.value != ''){
                let item = this.#fontSizeBoxContainer.querySelector(`[data-size="${this.#searchInputText.value}"]`);
                this.#apply(item, event);
            }
        },
    })

    #selectedFont;

    #defaultSampleText = '가나 다라 ab cd'
    #sampleText = this.#defaultSampleText;

    #applyCallback = () => {}

    #lastSelectionRange;

    #fontSizeObject;

    constructor(fontSizeObject){
        this.#fontSizeObject = fontSizeObject
        let style = document.querySelector(`#${this.#style.id}`);
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }

        
        let searchWrap = Object.assign(document.createElement('div'),{
            className: 'font-size-search-wrap'
        });
        searchWrap.append(this.#searchInputText);
        
        this.#fontSizeBox.append(searchWrap, this.#fontSizeBoxContainer);
        
        this.#fontSizeBox.append(this.#fontSizeBoxContainer);

    }

    #searchInputTextEvent(event){
        let number = Number(this.#searchInputText.value);
        if( ! this.#searchInputText.value || this.#searchInputText.value == '') {     
            this.#createFontElementList(this.#sampleText).then(fontElementList => {
                this.#fontSizeBoxContainer.replaceChildren(...fontElementList);
            });
            return;
        }else if(isNaN(number) || this.#fontSizeObject.min > number || this.#fontSizeObject.max < number){
            this.#fontSizeBoxContainer.replaceChildren();
            return;
        }

        let li = Object.assign(document.createElement('li'),{
            className: 'font-size-item',
        });
        if(this.#sampleText.nodeType && this.#sampleText.nodeType == Node.ELEMENT_NODE){
            li.innerHTML = this.#sampleText.innerHTML;
        }else{
            li.textContent = this.#sampleText;
        }
        li.style.fontSize = number + this.#fontSizeObject.unit;
        li.dataset.size = number;
        this.#addFontItemEvent(li);
        this.#fontSizeBoxContainer.replaceChildren(li);
        this.#searchInputText.focus();
    }

    #addFontItemEvent(item){
        return new Promise(res=>{
            item.onclick = (event) => this.#apply(item, event);
            res(item);
        });
    }

    #apply(item, event){
        this.#selectedFont = item;
        if(this.#lastSelectionRange){
            let selection = window.getSelection();
            selection.removeAllRanges();
            selection.addRange(this.#lastSelectionRange);
        }
        this.applyCallback(event);
    }

    #createFontElementList(sampleText){
        return new Promise(resolve=> {
            let list = [];
            let fixedLength = String(this.#fontSizeObject.weight).replace('.', '').length;
            for(let i = this.#fontSizeObject.min, len = this.#fontSizeObject.max + this.#fontSizeObject.weight ; 
                i < len ;
                i = parseFloat( (i + this.#fontSizeObject.weight).toFixed(fixedLength) )
            ){
                console.log(i);
                let li = Object.assign(document.createElement('li'),{
                    className: 'font-size-item',
                });
                if(sampleText.nodeType && sampleText.nodeType == Node.ELEMENT_NODE){
                    li.innerHTML = sampleText.innerHTML;
                }else{
                    li.textContent = sampleText;
                }
                li.style.fontSize = i + this.#fontSizeObject.unit;
                this.#addFontItemEvent(li);
                list.push(li);
            }
            resolve(list);
        })
    }

    async open(){
        this.#searchInputText.step = this.#fontSizeObject.weight;
        this.#searchInputText.min = this.#fontSizeObject.min;
        this.#searchInputText.max = this.#fontSizeObject.max;
        
        let selection = window.getSelection();
        if(selection.rangeCount != 0 && selection.isCollapsed == false){
            let range = selection.getRangeAt(0);
            this.#lastSelectionRange = range.cloneRange();
            let aticle = document.createElement('aticle');
            let rangeCloneContent = range.cloneContents();
            aticle.append(rangeCloneContent);
            this.#sampleText = aticle;
        }
        this.#sampleText = this.#sampleText == '' ? this.#defaultSampleText : this.#sampleText;
    
        document.body.append(this.#fontSizeBox);

        return await this.#createFontElementList(this.#sampleText).then(fontElementList => {
            this.#fontSizeBoxContainer.replaceChildren(...fontElementList);
            return this.#fontSizeBoxContainer;
        });
    }

    close(){
        this.#fontSizeBox.remove();
    }

    set applyCallback(applyCallback){
        this.#applyCallback = applyCallback;
    }

    get applyCallback(){
        return this.#applyCallback;
    }

    get fontSizeBox(){
        return this.#fontSizeBox;
    }

    get selectedFont(){
        return this.#selectedFont;
    }

	get style(){
		return this.#style;
	}

	set style(style){
        this.#style.textContent = style;
    }

	set insertStyle(style){
		this.#style.sheet.insertRule(style);
	}


    createStyle(){
        this.#style.textContent = `
            .font-size-wrap{
                background: #000000bf;
                position: fixed;
				padding: 0.9%;
				width: ${this.#fontSizeBoxVw}vw;
				height: 25vh;
				color: white;
                min-width: 200px;
                overflow-y: auto;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                overflow-x: hidden;
                z-index: 999;
            }

            .font-size-wrap .font-size-search-wrap{
                margin-bottom: 2%;
                overflow-x: hidden;
            }
            .font-size-wrap .font-size-search-wrap [name="font-size-search"]{
                outline: none;
                -moz-appearance: textfield;
                width: 100%;
                color: white;
            }
            .font-size-wrap .font-size-search-wrap [name="font-size-search"]::-webkit-outer-spin-button,
            .font-size-wrap .font-size-search-wrap [name="font-size-search"]::-webkit-inner-spin-button{
                -webkit-appearance: none;
                margin: 0;
            }

            .font-size-wrap .font-size-container{
                list-style-type: none;
                padding: 0;
                margin: 0;
            }
            .font-size-wrap .font-size-container .font-size-item{
                margin-bottom: 1%;
                transition: all 0.5s;
                overflow-x: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
            }
            .font-size-wrap .font-size-container .font-size-item:hover{
                background-color: #666666;
                cursor: pointer;
                background-color: #666666;
                padding: 2px 0 3px 0;
            }

        `
        return this.#style;
    }
}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/FontSize.js




class FontSize extends FreedomInterface {
    static toolHandler = new ToolHandler(this);

    static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-font-size-style'
	});

    static fontSizeBox;

	static unit = 'px';

	static min = 1;

	static max = 50;

	static weight = 1;

    static{
        
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-font-size';
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '↑↓',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Font Size'
        });

		this.fontSizeBox = new FontSizeBox(this);

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.fontSizeBox.fontSizeBox.isConnected){
				this.fontSizeBox.close();
			}else{
				this.fontSizeBox.open().then(fontSizeBoxContainer=>{
				    this.toolHandler.processingElementPosition(this.fontSizeBox.fontSizeBox);
                });
			}
		}

        this.fontSizeBox.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.fontSizeBox.close();
		}

        document.addEventListener("scroll", () => {
			if(this.fontSizeBox.fontSizeBox.isConnected){
				this.toolHandler.processingElementPosition(this.fontSizeBox.fontSizeBox);
			}
		});
        window.addEventListener('resize', (event) => {
            if(this.fontSizeBox.fontSizeBox.isConnected){
                this.fontSizeBox.open().then(fontSizeBoxContainer=>{
				    this.toolHandler.processingElementPosition(this.fontSizeBox.fontSizeBox);
                });
            }
		})

		super.outClickElementListener(this.fontSizeBox.fontSizeBox, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.fontSizeBox.fontSizeBox.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.fontSizeBox.close();
			}
		})
	}

    static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-size: 0.8rem;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	constructor(dataset){
		super(FontSize, dataset);
		if( ! dataset && Object.keys(this.dataset).length == 0){
            this.dataset.font_size = FontSize.fontSizeBox.selectedFont?.style.fontSize;
        }
        this.style.fontSize = this.dataset.font_size
    }
	
}

;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Italic.js


class Italic extends FreedomInterface {

	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-italic-style'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-italic';

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: 'I',
            className: `${this.#defaultStyle.id}-button`,
			title: 'Italic'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
				this.toolHandler.toolButton.dataset.tool_status = 'active';
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
				font-style: italic;
				font-size: 0.8rem;
			}

			.${this.toolHandler.defaultClass} {
				font-style: italic;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}
	
	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}


	constructor(dataset){
		super(Italic, dataset);
	}

	
}

;// CONCATENATED MODULE: ./view/js/handler/editor/component/ImageBox.js

class ImageBox {
    
    #style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-image-box'
	});

    #imageBox = Object.assign(document.createElement('div'), {
        className: 'image-box-wrap',
        
        innerHTML:`
            <div class="image-resize-container">
                <div>
                    <label class="image-box-resize-label" for="image-box-resize-width">width </label>
                    <input list="image-box-resize-datalist" class="image-box-resize-input" id="image-box-resize-width" type="number" autocomplete="off"/>
                    <div>
                        <label class="image-box-resize-label-error-message" for="image-box-resize-width"></label>
                    </div>
                </div>
                <div>
                    <label class="image-box-resize-label" for="image-box-resize-height">height(auto) </label>
                    <input list="image-box-resize-datalist" class="image-box-resize-input" id="image-box-resize-height" type="number" autocomplete="off" disabled/>
                </div>
            </div>
            <div class="image-key-description-container" style="display:none;">
                <kbd>Ctrl</kbd><kbd>Wheel</kbd>OR<kbd>Shift</kbd><kbd>Wheel</kbd>
            </div>
            <div class="image-button-container">
                <a href="javascript:void(0);" class="download" download>
                    <svg style="zoom:125%;" class="download-css-gg-push-down"
                        width="1rem"
                        height="1rem"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                        d="M11.0001 1H13.0001V15.4853L16.2428 12.2427L17.657 13.6569L12.0001 19.3137L6.34326 13.6569L7.75748 12.2427L11.0001 15.4853V1Z"
                        fill="#00000073"
                        />
                        <path d="M18 20.2877H6V22.2877H18V20.2877Z" fill="#00000073" />
                    </svg>
                </a>
                <a href="javascript:void(0);" class="new-window">
                    <svg style="zoom: 150%;" class="new-window-css-gg-expand"
                        width="1rem"
                        height="1rem"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                        d="M12.3062 16.5933L12.2713 18.593L5.2724 18.4708L5.39457 11.4719L7.39426 11.5068L7.33168 15.092L15.2262 7.46833L11.6938 7.40668L11.7287 5.40698L18.7277 5.52915L18.6055 12.5281L16.6058 12.4932L16.6693 8.85507L8.72095 16.5307L12.3062 16.5933Z"
                        fill="#00000073"
                        />
                    </svg>
                </a>
                <span class="image-editor">
                    <svg class="image-editor-css-gg-pen"
                        width="1rem"
                        height="1rem"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                        fill-rule="evenodd"
                        clip-rule="evenodd"
                        d="M21.2635 2.29289C20.873 1.90237 20.2398 1.90237 19.8493 2.29289L18.9769 3.16525C17.8618 2.63254 16.4857 2.82801 15.5621 3.75165L4.95549 14.3582L10.6123 20.0151L21.2189 9.4085C22.1426 8.48486 22.338 7.1088 21.8053 5.99367L22.6777 5.12132C23.0682 4.7308 23.0682 4.09763 22.6777 3.70711L21.2635 2.29289ZM16.9955 10.8035L10.6123 17.1867L7.78392 14.3582L14.1671 7.9751L16.9955 10.8035ZM18.8138 8.98525L19.8047 7.99429C20.1953 7.60376 20.1953 6.9706 19.8047 6.58007L18.3905 5.16586C18 4.77534 17.3668 4.77534 16.9763 5.16586L15.9853 6.15683L18.8138 8.98525Z"
                        fill="#00000073"
                        />
                        <path
                        d="M2 22.9502L4.12171 15.1717L9.77817 20.8289L2 22.9502Z"
                        fill="#00000073"
                        />
                    </svg>
                </span>
            </div>
        `
        /* 리사이즈 있는 버전 주석처리 20230821
        innerHTML: `
            <div class="image-resize-container">
                <div>
                    <label class="image-box-resize-label" for="image-box-resize-width">width : </label>
                    <input list="image-box-resize-datalist" class="image-box-resize-input" id="image-box-resize-width" type="number" autocomplete="off"/>
                </div>
                <div>
                    <label class="image-box-resize-label" for="image-box-resize-height">height(auto) : </label>
                    <input list="image-box-resize-datalist" class="image-box-resize-input" id="image-box-resize-height" type="number" autocomplete="off" disabled/>
                </div>
            </div>
            <div class="image-button-container">
                <a href="javascript:void(0);" class="download-css-gg-push-down" download></a>
                <a href="javascript:void(0);" class="new-window-css-gg-path-trim"></a>
            </div>
        `
        */
    });

    /*
    #removeEventPromiseResolve;
    #removeEventPromise = new Promise(resolve=>{
		this.#removeEventPromiseResolve = resolve;
	});
    */
    #image;
    #resizeRememberTarget;

    constructor(){
        let style = document.querySelector(`#${this.#style.id}`);
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }

        document.addEventListener('keydown',(event)=>{
            if(this.#imageBox.hasAttribute('data-is_shft')){
                return;
            }
            let {key} = event;
            if(key === 'Shift'){
                this.#imageBox.dataset.is_shft = '';
            }
        })

        document.addEventListener('keyup', (event)=>{
            if( ! this.#imageBox.hasAttribute('data-is_shft')){
                return;
            }    
            let {key} = event;
            if(key === 'Shift'){
                this.#imageBox.removeAttribute('data-is_shft');
            }
        })
        
        this.#imageBox.addEventListener('wheel', (event) => {
            if(this.#imageBox.hasAttribute('data-is_shft')){
                return;
            }
            //event.preventDefault();
            let {deltaY} = event;
            
            this.#imageBox.scrollTo(
                this.#imageBox.scrollLeft + deltaY, undefined
            );
        }, {passive: true})
        let [width, height] = this.#imageBox.querySelectorAll('#image-box-resize-width, #image-box-resize-height');
        
        window.addEventListener('keyup', (event) => {
            if( ! this.image || ! this.resizeRememberTarget || ! width.hasAttribute('data-is_ctrl') || ! this.image.parentElement.matches(':hover')){//|| this.image.getRootNode()?.activeElement != width){
                return;
            }
            width.removeAttribute('data-is_ctrl');
        })

        window.addEventListener('keydown', (event) => {
            
            let eventPath = event.composedPath()

            if( ! this.image || ! this.resizeRememberTarget || eventPath[0] == width || ! this.image.parentElement.matches(':hover')){//|| this.image.getRootNode()?.activeElement != width){
                return;
            }
            if(event.ctrlKey){
                width.dataset.is_ctrl = '';
            }else{
                width.removeAttribute('data-is_ctrl');
            }
        })
        
        /**
         * @see https://www.chromestatus.com/feature/6662647093133312
         */
        window.addEventListener('wheel', (event) => {
            
            if( ! this.image || ! this.resizeRememberTarget || ! this.image.parentElement.hasAttribute('data-is_resize_click') || event.composedPath()[0] == width || ! this.image.parentElement.matches(':hover')){// || this.image.getRootNode()?.activeElement != width){
                return;
            }
            if(width.hasAttribute('data-is_ctrl')){
                width.value = Number(width.value) + (event.deltaY * -1)
            }else{
                width.value = Number(width.value) + ((event.deltaY * -1) / 100)
            }
            this.oninputEvent(this.image, width, width, height, this.resizeRememberTarget);
        })
    }

    /**
     * 
     * @param {HTMLImageElement} image 
     */
    addImageHoverEvent(image, resizeRememberTarget){
        //image.parentElement.onmouseover = () => {
        let keyDescription = this.#imageBox.querySelector('.image-key-description-container')   
        image.parentElement.onmouseenter = () => {
            if(! image.src || image.src == '' || image.hasAttribute('data-error')){
                return;
            }
             let root = image.getRootNode();
            if(root != document){
                root.append(this.#style);
            }else{
                document.head.append(this.#style);
            }

            if(image.parentElement && (image.parentElement !== this.#imageBox.parentElement || ! this.#imageBox.classList.contains('start'))){
                image.parentElement.append(this.#imageBox);

                this.#addRresizeEvent(image, resizeRememberTarget)
                this.#addButtonIconEvent(image)
                let appendAwait = setInterval(()=>{
                    if(this.#imageBox.isConnected && image.complete && image.parentElement === this.#imageBox.parentElement && ! this.#imageBox.classList.contains('start')){
                        this.#imageBox.classList.add('start');
                        this.image = image;
                        this.resizeRememberTarget = resizeRememberTarget;
                        image.parentElement.onclick = (event) => {
                            if(! image.src || image.src == '' || event.composedPath()[0] != image || image.hasAttribute('data-error')){
                                return;
                            }
                            this.#imageBox.classList.add('start');
                            image.parentElement.toggleAttribute('data-is_resize_click');
                            this.falsh(image.parentElement);
                            if(image.parentElement.hasAttribute('data-is_resize_click')){
                                keyDescription.style.display = '';
                            }else {
                                keyDescription.style.display = 'none';
                            }
                        }
                        clearInterval(appendAwait);
                    }
                }, 50)
            }
        }
        image.parentElement.onmouseleave = () => {
            this.#imageBox.classList.remove('start');
            if(image.parentElement.hasAttribute('data-is_resize_click')){
                keyDescription.style.display = 'none';
                this.falsh(image.parentElement);
            }
            image.parentElement.removeAttribute('data-is_resize_click');
            /*if(this.#imageBox.isConnected && image.parentElement === this.#imageBox.parentElement){
                
                this.#imageBox.classList.remove('start');
                this.#imageBox.ontransitionend = () => {
                    if(this.#imageBox.isConnected){
                        this.#imageBox.remove();
                    }
                }
                
            }*/
        }
    }

    falsh(target){
        return new Promise(resolve => {
            let flash = document.createElement('div');
            Object.assign(flash.style, {
                position: 'absolute',top: '0px',left: '0px',
                width: '100%',height: '100%', background: 'rgba(255, 255, 255, 0.4)',
                transition: 'opacity 0.2s ease 0s', opacity: 0
            })
            target.append(flash);
            let flashAwait = setInterval(()=>{
                if( ! flash.isConnected){
                    return; 
                }
                clearInterval(flashAwait);
                flash.style.opacity = 1;
                flash.ontransitionend = () => {
                    flash.style.opacity = 0;
                    flash.ontransitionend = () => {
                        flash.remove();
                        resolve();
                    }
                }
            }, 50)
        });
    }

    /**
     * 
     * @param {HTMLImageElement} image 
     */
    //리사이즈 있는 버전 주석 처리 20230821
    #addRresizeEvent(image, resizeRememberTarget){
        return new Promise(resolve => {
            let [width, height] = this.#imageBox.querySelectorAll('#image-box-resize-width, #image-box-resize-height');
            width.value = image.width, height.value = image.height;
            
            width.labels[0].textContent = 'width : ';
            width.labels[1].textContent = '';
            
            this.prevValue = undefined;

            width.oninput = (event) => this.oninputEvent(image, event.target, width, height, resizeRememberTarget);
            width.onkeydown = (event) => {
                if(event.ctrlKey){
                    width.dataset.is_ctrl = '';
                }else{
                    width.removeAttribute('data-is_ctrl');
                }
            }
            width.onkeyup = (event) => {
                width.removeAttribute('data-is_ctrl');
            }
            width.onblur = () => {
                width.removeAttribute('data-is_ctrl');
            }
            width.onwheel = (event) => {
                event.preventDefault();
                if(width.hasAttribute('data-is_ctrl')){
                    width.value = Number(width.value) + (event.deltaY * -1)
                }else{
                    width.value = Number(width.value) + ((event.deltaY * -1) / 100)
                }
                this.oninputEvent(image, event.target, width, height, resizeRememberTarget);
            }
            //height.oninput = (event) => oninputEvent(event);
            resolve({width, height});
        });
    }
    oninputEvent(image, target, width, height, resizeRememberTarget) {
        if(isNaN(Number(target.value))){
            target.value = target.value.replace(/\D/g, '');
            return;
        }else if(Number(target.value) < 50){
            width.labels[1].textContent = '(min 50)';
            target.value = 50;
        }else{
            width.labels[1].textContent = '';
        }
        let sizeName = target.id.includes('width') ? 'width': 'height';
        image[sizeName] = target.value;

        let imageRect = image.getBoundingClientRect();

        //width.value = image.width, height.value = image.height;
        if(this.prevValue && parseInt(this.prevValue) == parseInt(imageRect.width)){
            width.value = parseInt(this.prevValue);
            width.labels[1].textContent = `(max ${parseInt(this.prevValue)}) : `
        }
        this.prevValue = imageRect.width;
        resizeRememberTarget.dataset.width = width.value;
    }
    #addButtonIconEvent(image){
        return new Promise(resolve => {
            let [download, newWindow] = [...this.#imageBox.querySelectorAll('.download-css-gg-push-down, .new-window-css-gg-expand')]
                .map(e=>e.parentElement)
            download.href = image.src, newWindow.href = image.src
            download.download = image.dataset.image_name;
            newWindow.target = '_blank';
            resolve({download, newWindow});
        })
    }

    get imageBox(){
        return this.#imageBox;
    }

	get style(){
		return this.#style;
	}

	set style(style){
        this.#style.textContent = style;
    }

	set insertStyle(style){
		this.#style.sheet.insertRule(style);
	}
    
    set image(image){
        this.#image = image; 
    }

    get image(){
        return this.#image;
    }
    set resizeRememberTarget(resizeRememberTarget){
        this.#resizeRememberTarget = resizeRememberTarget;
    }
    get resizeRememberTarget(){
        return this.#resizeRememberTarget;
    }

    createStyle(){
        this.#style.textContent = `
            .image-box-wrap{
                position: absolute;
                display: flex;
                justify-content: space-between;
                width: 100%;
                background: linear-gradient(to bottom, #ff8787 -73%, #ffffffcf 115%);
                color: white;
                top:-20%;
                opacity: 0;
                transition: all 1s;
                white-space: nowrap;
                overflow-y: clip;
                overflow-x: auto;
            }
            .image-box-wrap::-webkit-scrollbar{
                display: none;
            }
            .image-box-wrap:hover::-webkit-scrollbar{
                display: initial;
                width: 7px;
                height: 7px;
            }
            .image-box-wrap:hover::-webkit-scrollbar-track{
                background: #00000040;
                border-radius: 100px;
                box-shadow: inset 0 0 5px #000000fc;
            }
            .image-box-wrap:hover::-webkit-scrollbar-thumb {
                background: #0c0c0c38;
                border-radius: 100px;
                box-shadow: inset 0 0 5px #000000;
            }
            .image-box-wrap:hover::-webkit-scrollbar-thumb:hover {
                /*background: #44070757;*/
                background: #34000075; 
            }
            .image-box-wrap.start{
                top: 0;
                opacity: 1;
                transition: all 0.5s;
            }
            .image-box-wrap .image-button-container,
            .image-box-wrap .image-resize-container, 
            .image-box-wrap .image-key-description-container{
                display: flex;
                gap: 1.5vw;
                padding: 1.7%;
                align-items: center;
            }
            .image-box-wrap .image-resize-container .image-box-resize-label{
                background: linear-gradient(to right, #e50bff, #004eff);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }
            .image-box-wrap .image-resize-container .image-box-resize-input{
                outline: none;
                border: none;
                background-image: linear-gradient(#fff1f1d4, #ffffffeb), linear-gradient(to right, #a189890d 0%,  #ed89b275 100%);
                background-origin: border-box;
                background-clip: content-box, border-box;
                background-color: #00000000; 
                width: 3.2rem;
                height: 100%;
                color: #ffb6b6;
                font-size: 0.9rem;
                text-align: center;
            }
            .image-box-wrap .image-button-container .new-window,
            .image-box-wrap .image-button-container .download,
            .image-box-wrap .image-button-container .image-editor{
                display: flex;
                align-items: center;
                border: none;
                background:none;
                position:relative;
                cursor: pointer;
            }

            .image-box-wrap kbd {
                background-color: #eee;
                border-radius: 3px;
                border: 1px solid #b4b4b4;
                box-shadow:
                0 1px 1px rgba(0, 0, 0, 0.2),
                0 2px 0 0 rgba(255, 255, 255, 0.7) inset;
                color: #333;
                display: inline-block;
                font-size: 0.85em;
                font-weight: 700;
                line-height: 1;
                padding: 2px 4px;
                white-space: nowrap;
                height: fit-content;
            }
        `
        return this.#style;
    }
}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Image.js




class Image extends FreedomInterface {

	static toolHandler = new ToolHandler(this);

    static imageBox = new ImageBox();

    static customImageCallback; 

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-image-style'
	});

    static slotName = 'free-will-editor-image-description-slot';

    static #selectedFile = Object.assign(document.createElement('input'), {
        type: 'file',
        accept: 'image/*'
    });

    static get selectedFile(){
        return this.#selectedFile;
    }

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-image';
        //this.toolHandler.isInline = false;

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '',
            className: `${this.#defaultStyle.id}-button`,
            innerHTML: `
            <svg class="${this.#defaultStyle.id} css-gg-image-icon"
                width="0.9rem"
                height="0.9rem"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
                >
                <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M7 7C5.34315 7 4 8.34315 4 10C4 11.6569 5.34315 13 7 13C8.65685 13 10 11.6569 10 10C10 8.34315 8.65685 7 7 7ZM6 10C6 9.44772 6.44772 9 7 9C7.55228 9 8 9.44772 8 10C8 10.5523 7.55228 11 7 11C6.44772 11 6 10.5523 6 10Z"
                    fill="currentColor"
                />
                <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M3 3C1.34315 3 0 4.34315 0 6V18C0 19.6569 1.34315 21 3 21H21C22.6569 21 24 19.6569 24 18V6C24 4.34315 22.6569 3 21 3H3ZM21 5H3C2.44772 5 2 5.44772 2 6V18C2 18.5523 2.44772 19 3 19H7.31374L14.1924 12.1214C15.364 10.9498 17.2635 10.9498 18.435 12.1214L22 15.6863V6C22 5.44772 21.5523 5 21 5ZM21 19H10.1422L15.6066 13.5356C15.9971 13.145 16.6303 13.145 17.0208 13.5356L21.907 18.4217C21.7479 18.7633 21.4016 19 21 19Z"
                    fill="currentColor"
                />
            </svg>
            `,
            title: 'Image'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
                this.#selectedFile.click();
                this.#selectedFile.onchange = ()=> {
                    //let url = URL.createObjectURL(this.#selectedFile.files[0])
                    this.toolHandler.toolButton.dataset.tool_status = 'active';
                }
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
            .${this.toolHandler.defaultClass} {
                position: relative;
            }
            .${this.#defaultStyle.id}.css-gg-image-icon {
                zoom:120%;
            }
            .${this.#defaultStyle.id}.image-description{            
                cursor: pointer;
                display: inline-flex;
                align-items: center;
            }

            .${this.#defaultStyle.id}.image-description::after{
                margin-left: 0.5em;
                content: ' ['attr(data-file_name)'] 'attr(data-open_status);
                font-size: small;
                color: #bdbdbd;
            }

            .${this.#defaultStyle.id}.image-contanier{
                width: fit-content;
                transition: height 0.5s ease-in-out;
                overflow: hidden;
                position: relative;
            }
            .${this.#defaultStyle.id}.image-contanier img{
                max-width: 100%;
                height: auto;
                aspect-ratio: attr(width) / attr(height);
                image-rendering: crisp-edges;
                image-rendering: -webkit-optimize-contrast;
            }
        ` 
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

    static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

    #file;

    imgLoadEndCallback = (event) => {};

    image = document.createElement('img');

	constructor(dataset){
		super(Image, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});
        
        if( ! dataset && Object.keys(this.dataset).length == 0){
            this.#file = Image.selectedFile.cloneNode(true);
            Image.selectedFile.files = new DataTransfer().files;
            
            this.dataset.url = URL.createObjectURL(this.#file.files[0]);
            this.dataset.name = this.#file.files[0].name;
            this.dataset.last_modified = this.#file.files[0].lastModified;
            this.dataset.size = this.#file.files[0].size;
            this.dataset.content_type = this.#file.files[0].type;

            let url = URL.createObjectURL(this.#file.files[0], this.dataset.content_type);
            this.dataset.url = url;
            this.image.type = this.dataset.content_type;
            this.image.src = this.dataset.url;
            /*fetch(this.dataset.url).then(res=>res.blob()).then(blob => {
                const reader = new FileReader();
                reader.readAsDataURL(blob);
                reader.onloadend = () => {
                    this.dataset.base64 = reader.result;
                }
            })*/
            
        }else if(( ! this.dataset.url || this.dataset.url.startsWith('blob:file')) && this.dataset.base64){
            fetch(this.dataset.base64)
            .then(async res=>{
                return res.blob().then(blob=>{
                    let imgUrl = URL.createObjectURL(blob, res.headers.get('Content-Type'))
                    this.dataset.url = imgUrl;
                    this.image.src = this.dataset.url;
                })
            })
        }else if(Image.customImageCallback && typeof Image.customImageCallback == 'function'){
            Image.customImageCallback(this);
        }else if(this.dataset.url){
            this.image.src = this.dataset.url;
        }

        if(! this.dataset.name){
            this.remove();
            throw new Error(`this file is undefined ${this.dataset.name}`);
        }
        
        this.attachShadow({ mode : 'open' });
        this.shadowRoot.append(Image.defaultStyle.cloneNode(true));
        
        this.createDefaultContent().then(({wrap, description, slot, aticle})=>{
            this.connectedChildAfterCallback = (addedList) => {
                aticle.append(...addedList);
            }
        });
        
        this.disconnectedAfterCallback = () => {
            if(this.dataset.url.startsWith('blob:file')){
                setTimeout(() => {
                    URL.revokeObjectURL(this.dataset.url);
                }, 1000 * 60 * 2)
            }
        }
	}

    createDefaultContent(){
        return new Promise(resolve=>{
            let wrap = Object.assign(document.createElement('div'),{

            });
            wrap.draggable = false

            this.shadowRoot.append(wrap);

            let imageContanier = Object.assign(document.createElement('div'),{
                className: `${Image.defaultStyle.id} image-contanier`
            });

            /*let image = Object.assign(document.createElement('img'), {
                //src :`https://developer.mozilla.org/pimg/aHR0cHM6Ly9zLnprY2RuLm5ldC9BZHZlcnRpc2Vycy9iMGQ2NDQyZTkyYWM0ZDlhYjkwODFlMDRiYjZiY2YwOS5wbmc%3D.PJLnFds93tY9Ie%2BJ%2BaukmmFGR%2FvKdGU54UJJ27KTYSw%3D`
                //src: this.dataset.url
                //src: imgUrl
            });*/

            //if(this.#selectedFile.files.length != 0){
            this.image.dataset.image_name = this.dataset.name
            //}

            imageContanier.append(this.image);

            this.image.onload = () => {
                if(this.dataset.width){
                    this.image.width = this.dataset.width;
                }
                /*let applyToolAfterSelection = window.getSelection(), range = applyToolAfterSelection.getRangeAt(0);
                let scrollTarget;
                if(range.endContainer.nodeType == Node.TEXT_NODE){
                    scrollTarget = range.endContainer.parentElement
                }else{
                    scrollTarget = range.endContainer;
                }
                scrollTarget.scrollIntoView({ behavior: "instant", block: "end", inline: "nearest" });
                */
            //this.imgLoadEndCallback();
                //imageContanier.style.height = window.getComputedStyle(image).height;
            }
            this.image.onerror = () => {
                //imageContanier.style.height = window.getComputedStyle(image).height;
                this.image.dataset.error = '';
            }

            let {description, slot, aticle} = this.createDescription(this.image, imageContanier);

            this.connectedAfterOnlyOneCallback = () => {
                if(this.childNodes.length != 0 && this.childNodes[0]?.tagName != 'BR'){
                    aticle.append(...[...this.childNodes].filter(e=>e!=aticle));
                }
                wrap.replaceChildren(...[description,imageContanier].filter(e=>e != undefined));
                
                Image.imageBox.addImageHoverEvent(this.image, this);
                if(this.nextSibling?.tagName == 'BR'){
                    this.nextSibling.remove()
                }

                resolve({wrap, description, slot, aticle})
            }
        })
    }

    /**
     * 
     * @param {HTMLImageElement} image 
     * @param {HTMLDivElement} imageContanier 
     * @returns {HTMLDivElement}
     */
    createDescription(image, imageContanier){
        let description = Object.assign(document.createElement('div'),{
            className: `${Image.defaultStyle.id} image-description`
        });

        description.dataset.file_name = this.dataset.name
        description.dataset.open_status = this.dataset.open_status || '▼'; // '▼';
        imageContanier.style.height = this.dataset.height || 'auto'
        
        let {slot, aticle} = this.createSlot();
        
        description.append(slot)

        description.onclick = (event) => {
            if(description.dataset.open_status == '▼'){
                description.dataset.open_status = '▶'
                imageContanier.style.height = window.getComputedStyle(image).height;
                setTimeout(()=>{
                    imageContanier.style.height = '0px';
                    this.dataset.height = '0px';
                },100)

            }else{
                description.dataset.open_status = '▼';
                setTimeout(()=>{
                    imageContanier.style.height = window.getComputedStyle(image).height;
                    this.dataset.height = 'auto'
                },100)
                
                image.style.opacity = '';
                image.style.visibility = '';
            }
            this.dataset.open_status = description.dataset.open_status;
        }

        imageContanier.ontransitionend = () => {
            if(description.dataset.open_status == '▼'){
                imageContanier.style.height = 'auto';
            }else{
                image.style.opacity = 0;
                image.style.visibility = 'hidden';
            }
        }

        return {description, slot, aticle};
    }

    /**
     * 
     * @returns {HTMLSlotElement}
     */
    createSlot(){
        let aticle = document.createElement('div');
        
        aticle.contentEditable = 'false';
        aticle.draggable = 'false'; 

        //if(this.childNodes.length != 0 && this.childNodes[0]?.tagName != 'BR'){
            let randomId = Array.from(
                window.crypto.getRandomValues(new Uint32Array(16)),
                (e)=>e.toString(32).padStart(2, '0')
            ).join('');
            //aticle.append(...[...this.childNodes].map(e=>e.cloneNode(true)));
            aticle.append(...this.childNodes);
            aticle.slot = Image.slotName + '-' + randomId
            this.append(aticle);
            
            let slot = Object.assign(document.createElement('slot'),{
                name: Image.slotName + '-' + randomId
            });
            return {slot, aticle};
        //}else{
        //    return undefined
        //}

    }
    /**
     * @returns {HTMLInputElement}
     */
    get selectedFile(){
        return this.#file
    }
}

;// CONCATENATED MODULE: ./view/js/handler/editor/component/VideoBox.js


class VideoBox {
    
    #style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-video-box'
	});

    #videoBox = Object.assign(document.createElement('div'), {
        className: 'video-box-wrap',
        
        innerHTML:`
            <div class="video-resize-container">
                <div>
                    <label class="video-box-resize-label" for="video-box-resize-width">width : </label>
                    <input list="video-box-resize-datalist" class="video-box-resize-input" id="video-box-resize-width" type="number" autocomplete="off"/>
                    <div>
                        <label class="video-box-resize-label-error-message" for="video-box-resize-width"></label>
                    </div>
                </div>
                <div>
                    <label class="video-box-resize-label" for="video-box-resize-height">height(auto) : </label>
                    <input list="video-box-resize-datalist" class="video-box-resize-input" id="video-box-resize-height" type="number" autocomplete="off" disabled/>
                </div>
            </div>
            <div class="video-key-description-container" style="display:none;">
                <kbd>Ctrl</kbd><kbd>Wheel</kbd>OR<kbd>Shift</kbd><kbd>Wheel</kbd>
            </div>
            <div class="video-button-container">
                <a href="javascript:void(0);" class="download" download>
                    <svg style="zoom:140%;" class="download-css-gg-push-down"
                        width="1rem"
                        height="1rem"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                        d="M11.0001 1H13.0001V15.4853L16.2428 12.2427L17.657 13.6569L12.0001 19.3137L6.34326 13.6569L7.75748 12.2427L11.0001 15.4853V1Z"
                        fill="#00000073"
                        />
                        <path d="M18 20.2877H6V22.2877H18V20.2877Z" fill="#00000073" />
                    </svg>
                </a>
                <a href="javascript:void(0);" class="new-window">
                    <svg style="zoom: 150%;" class="new-window-css-gg-expand"
                        width="1rem"
                        height="1rem"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                        d="M12.3062 16.5933L12.2713 18.593L5.2724 18.4708L5.39457 11.4719L7.39426 11.5068L7.33168 15.092L15.2262 7.46833L11.6938 7.40668L11.7287 5.40698L18.7277 5.52915L18.6055 12.5281L16.6058 12.4932L16.6693 8.85507L8.72095 16.5307L12.3062 16.5933Z"
                        fill="#00000073"
                        />
                    </svg>
                </a>
            </div>
        `
        /* 리사이즈 있는 버전 주석처리 20230821 <i class="download-css-gg-push-down"></i>
        innerHTML: `
            <div class="video-resize-container">
                <div>
                    <label class="video-box-resize-label" for="video-box-resize-width">width : </label>
                    <input list="video-box-resize-datalist" class="video-box-resize-input" id="video-box-resize-width" type="number" autocomplete="off"/>
                </div>
                <div>
                    <label class="video-box-resize-label" for="video-box-resize-height">height(auto) : </label>
                    <input list="video-box-resize-datalist" class="video-box-resize-input" id="video-box-resize-height" type="number" autocomplete="off" disabled/>
                </div>
            </div>
            <div class="video-button-container">
                <a href="javascript:void(0);" class="download-css-gg-push-down" download></a>
                <a href="javascript:void(0);" class="new-window-css-gg-path-trim"></a>
            </div>
        `
        */
    });
    /*
    #removeEventPromiseResolve;
    #removeEventPromise = new Promise(resolve=>{
		this.#removeEventPromiseResolve = resolve;
	});
    */
    #video;
    #resizeRememberTarget;

    constructor(){
        let style = document.querySelector(`#${this.#style.id}`);
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }
        document.addEventListener('keydown',(event)=>{
            if(this.#videoBox.hasAttribute('data-is_shft')){
                return;
            }
            let {key} = event;
            if(key === 'Shift'){
                this.#videoBox.dataset.is_shft = '';
            }
        })

        document.addEventListener('keyup', (event)=>{
            if( ! this.#videoBox.hasAttribute('data-is_shft')){
                return;
            }    
            let {key} = event;
            if(key === 'Shift'){
                this.#videoBox.removeAttribute('data-is_shft');
            }
        })
        
        this.#videoBox.addEventListener('wheel', (event) => {
            if(this.#videoBox.hasAttribute('data-is_shft')){
                return;
            }
            //event.preventDefault();
            let {deltaY} = event;
            
            this.#videoBox.scrollTo(
                this.#videoBox.scrollLeft + deltaY, undefined
            );
        }, {passive: true});
        let [width, height] = this.#videoBox.querySelectorAll('#video-box-resize-width, #video-box-resize-height');
        
        window.addEventListener('keyup', (event) => {
            if( ! this.video || ! this.resizeRememberTarget || ! width.hasAttribute('data-is_ctrl') || ! this.video.parentElement.matches(':hover')){//|| this.video.getRootNode()?.activeElement != width){
                return;
            }
            width.removeAttribute('data-is_ctrl');
        })

        window.addEventListener('keydown', (event) => {
            
            let eventPath = event.composedPath()

            if( ! this.video || ! this.resizeRememberTarget || eventPath[0] == width || ! this.video.parentElement.matches(':hover')){//|| this.video.getRootNode()?.activeElement != width){
                return;
            }
            if(event.ctrlKey){
                width.dataset.is_ctrl = '';
            }else{
                width.removeAttribute('data-is_ctrl');
            }
        })
        
        /**
         * @see https://www.chromestatus.com/feature/6662647093133312
         */
        window.addEventListener('wheel', (event) => {

            if( ! this.video || ! this.resizeRememberTarget || ! this.video.parentElement.hasAttribute('data-is_resize_click') || event.composedPath()[0] == width || ! this.video.parentElement.matches(':hover')){// || this.video.getRootNode()?.activeElement != width){
                return;
            }
            if(width.hasAttribute('data-is_ctrl')){
                width.value = Number(width.value) + (event.deltaY * -1)
            }else{
                width.value = Number(width.value) + ((event.deltaY * -1) / 100)
            }
            this.oninputEvent(this.video, width, height, this.resizeRememberTarget);
            
        })
    }

    /**
     * 
     * @param {HTMLVideoElement} video 
     */
    addVideoHoverEvent(video, resizeRememberTarget){
        //video.parentElement.onmouseover = () => {
        let keyDescription = this.#videoBox.querySelector('.video-key-description-container')
        video.parentElement.onmouseenter = () => {
            if(! video.src || video.src == '' || video.hasAttribute('data-error')){
                return;
            }
            let root = video.getRootNode();
            if(root != document){
                root.append(this.#style);
            }else{
                document.head.append(this.#style);
            }

            if(video.parentElement && (video.parentElement !== this.#videoBox.parentElement || ! this.#videoBox.classList.contains('start'))){
                video.parentElement.append(this.#videoBox);

                this.#addRresizeEvent(video, resizeRememberTarget)
                this.#addButtonIconEvent(video)
                let appendAwait = setInterval(()=>{
                    if(this.#videoBox.isConnected && video.readyState == 4 && video.parentElement === this.#videoBox.parentElement && ! this.#videoBox.classList.contains('start')){
                        this.#videoBox.classList.add('start');
                        this.video = video;
                        this.resizeRememberTarget = resizeRememberTarget;
                        video.parentElement.onclick = (event) => {
                            if(! video.src || video.src == '' || event.composedPath()[0] != video || video.hasAttribute('data-error')){
                                return;
                            }
                            video.parentElement.toggleAttribute('data-is_resize_click');
                            this.falsh(video.parentElement);
                            if(video.parentElement.hasAttribute('data-is_resize_click')){
                                keyDescription.style.display = '';
                            }else {
                                keyDescription.style.display = 'none';
                            }
                        }
                        clearInterval(appendAwait);
                    }
                }, 50)
            }
        }
        video.parentElement.onmouseleave = () => {
            this.#videoBox.classList.remove('start');
            if(video.parentElement.hasAttribute('data-is_resize_click')){
                keyDescription.style.display = 'none';
                this.falsh(video.parentElement);
            }
            video.parentElement.removeAttribute('data-is_resize_click');
            /*if(this.#videoBox.isConnected && video.parentElement === this.#videoBox.parentElement){
                
                this.#videoBox.classList.remove('start');
                this.#videoBox.ontransitionend = () => {
                    if(this.#videoBox.isConnected){
                        this.#videoBox.remove();
                    }
                }
                
            }*/
        }
    }

    falsh(target){
        return new Promise(resolve => {
            let flash = document.createElement('div');
            Object.assign(flash.style, {
                position: 'absolute',top: '0px',left: '0px',
                width: '100%',height: '100%', background: 'rgba(255, 255, 255, 0.4)',
                transition: 'opacity 0.2s ease 0s', opacity: 0
            })
            target.append(flash);
            let flashAwait = setInterval(()=>{
                if( ! flash.isConnected){
                    return; 
                }
                clearInterval(flashAwait);
                flash.style.opacity = 1;
                flash.ontransitionend = () => {
                    flash.style.opacity = 0;
                    flash.ontransitionend = () => {
                        flash.remove();
                        resolve();
                    }
                }
            }, 50)
        });
    }

    /**
     * 
     * @param {HTMLVideoElement} video 
     */
    //리사이즈 있는 버전 주석 처리 20230821
    #addRresizeEvent(video, resizeRememberTarget){
        return new Promise(resolve => {
            let [width, height] = this.#videoBox.querySelectorAll('#video-box-resize-width, #video-box-resize-height');
            let rect = video.getBoundingClientRect();
            width.value = parseInt(rect.width), height.value = parseInt(rect.height);
            
            width.labels[0].textContent = 'width : ';
            width.labels[1].textContent = '';

            this.prevValue = undefined;

            width.oninput = (event) => this.oninputEvent(video, width, height, resizeRememberTarget);
            width.onkeydown = (event) => {
                if(event.ctrlKey){
                    width.dataset.is_ctrl = '';
                }else{
                    width.removeAttribute('data-is_ctrl');
                }
            }
            width.onkeyup = (event) => {
                width.removeAttribute('data-is_ctrl');
            }
            width.onblur = () => {
                width.removeAttribute('data-is_ctrl');
            }
            width.onwheel = (event) => {
                event.preventDefault();
                if(width.hasAttribute('data-is_ctrl')){
                    width.value = Number(width.value) + (event.deltaY * -1)
                }else{
                    width.value = Number(width.value) + ((event.deltaY * -1) / 100)
                }

                this.oninputEvent(video, width, height, resizeRememberTarget);
            }
            //height.oninput = (event) => oninputEvent(event);
            resolve({width, height});
        });
    }
    oninputEvent(video, width, height, resizeRememberTarget) {
        if(isNaN(Number(width.value))){
            width.value = width.value.replace(/\D/g, '');
            return;
        }else if(Number(width.value) < 50){
            width.labels[1].textContent = '(min 50)';
            width.value = 50;
        }else{
            width.labels[1].textContent = '';
        }
        let sizeName = width.id.includes('width') ? 'width': 'height';
        video[sizeName] = width.value;

        let videoRect = video.getBoundingClientRect();

        if(this.prevValue && parseInt(this.prevValue) == parseInt(videoRect.width)){
            width.value = parseInt(this.prevValue);
            width.labels[1].textContent = `(max ${parseInt(this.prevValue)}) : `
        }
        this.prevValue = videoRect.width

        resizeRememberTarget.dataset.width = width.value;
    }
    #addButtonIconEvent(video){
        return new Promise(resolve => {
            let [download, newWindow] = [...this.#videoBox.querySelectorAll('.download-css-gg-push-down, .new-window-css-gg-expand')]
                .map(e=>e.parentElement)
            download.href = video.src, newWindow.href = video.src;
            download.download = video.dataset.video_name;
            newWindow.target = '_blank';
            resolve({download, newWindow});
        })
    }

    get videoBox(){
        return this.#videoBox;
    }

	get style(){
		return this.#style;
	}

	set style(style){
        this.#style.textContent = style;
    }

	set insertStyle(style){
		this.#style.sheet.insertRule(style);
	}
    
    set video(video){
        this.#video = video; 
    }

    get video(){
        return this.#video;
    }
    set resizeRememberTarget(resizeRememberTarget){
        this.#resizeRememberTarget = resizeRememberTarget;
    }
    get resizeRememberTarget(){
        return this.#resizeRememberTarget;
    }

    createStyle(){
        this.#style.textContent = `
            .video-box-wrap{
                position: absolute;
                display: flex;
                justify-content: space-between;
                width: 100%;
                background: linear-gradient(to bottom, #ff8787 -73%, #ffffffcf 115%);
                color: white;
                top:-20%;
                opacity: 0;
                transition: all 1s;
                white-space: nowrap;
                overflow-y: clip;
                overflow-x: auto;
            }
            .video-box-wrap::-webkit-scrollbar{
                display: none;
            }
            .video-box-wrap:hover::-webkit-scrollbar{
                display: initial;
                width: 7px;
                height: 7px;
            }
            .video-box-wrap:hover::-webkit-scrollbar-track{
                background: #00000040;
                border-radius: 100px;
                box-shadow: inset 0 0 5px #000000fc;
            }
            .video-box-wrap:hover::-webkit-scrollbar-thumb {
                background: #0c0c0c38;
                border-radius: 100px;
                box-shadow: inset 0 0 5px #000000;
            }
            .video-box-wrap:hover::-webkit-scrollbar-thumb:hover {
                /*background: #44070757;*/
                background: #34000075; 
            }
            .video-box-wrap.start{
                top: 0;
                opacity: 1;
                transition: all 0.5s;
            }
            .video-box-wrap .video-button-container, 
            .video-box-wrap .video-resize-container,
            .video-box-wrap .video-key-description-container{
                display: flex;
                gap: 1.5vw;
                padding: 1.7%;
                align-items: center;
            }
            .video-box-wrap .video-resize-container .video-box-resize-label{
                background: linear-gradient(to right, #e50bff, #004eff);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }
            .video-box-wrap .video-resize-container .video-box-resize-input{
                outline: none;
                border: none;
                background-image: linear-gradient(#fff1f1d4, #ffffffeb), linear-gradient(to right, #a189890d 0%,  #ed89b275 100%);
                background-origin: border-box;
                background-clip: content-box, border-box;
                background-color: #00000000; 
                width: 3.2rem;
                height: 100%;
                color: #ffb6b6;
                font-size: 0.9rem;
                text-align: center;
            }
            .video-box-wrap .video-button-container .new-window,
            .video-box-wrap .video-button-container .download{
                display: flex;
                align-items: center;
                border: none;
                background:none;
                position:relative;
                cursor: pointer;
            }

            .video-box-wrap kbd {
                background-color: #eee;
                border-radius: 3px;
                border: 1px solid #b4b4b4;
                box-shadow:
                0 1px 1px rgba(0, 0, 0, 0.2),
                0 2px 0 0 rgba(255, 255, 255, 0.7) inset;
                color: #333;
                display: inline-block;
                font-size: 0.85em;
                font-weight: 700;
                line-height: 1;
                padding: 2px 4px;
                white-space: nowrap;
                height: fit-content;
            }
        `
        return this.#style;
    }

}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Video.js




class Video extends FreedomInterface {

	static toolHandler = new ToolHandler(this);

    static videoBox = new VideoBox();

    static customVideoCallback;

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-video-style'
	});

    static descriptionName = 'free-will-editor-video-description-slot';

    static #selectedFile = Object.assign(document.createElement('input'), {
        type: 'file',
        accept: 'video/*'
    });

    static get selectedFile(){
        return this.#selectedFile;
    }

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-video';

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '',
            className: `${this.#defaultStyle.id}-button`,
            innerHTML: `
            <svg class="${this.#defaultStyle.id} css-gg-video-icon"
            width="0.9rem"
            height="0.9rem"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            >
                <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M4 4.5V6.5H12V7.5H3C1.34315 7.5 0 8.84315 0 10.5V16.5C0 18.1569 1.34315 19.5 3 19.5H15C16.5731 19.5 17.8634 18.2892 17.9898 16.7487L24 17.5V9.5L17.9898 10.2513C17.8634 8.71078 16.5731 7.5 15 7.5H14V5.5C14 4.94772 13.5523 4.5 13 4.5H4ZM18 12.2656V14.7344L22 15.2344V11.7656L18 12.2656ZM16 10.5C16 9.94772 15.5523 9.5 15 9.5H3C2.44772 9.5 2 9.94772 2 10.5V16.5C2 17.0523 2.44772 17.5 3 17.5H15C15.5523 17.5 16 17.0523 16 16.5V10.5Z"
                    fill="currentColor"
                />
            </svg>
            `,
            title: 'Video'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
                this.#selectedFile.onchange = ()=> {
                    //let url = URL.createObjectURL(this.#selectedFile.files[0])
                    this.toolHandler.toolButton.dataset.tool_status = 'active';
                }
                this.#selectedFile.click();
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
            .${this.toolHandler.defaultClass} {
                position: relative;
            }
            .${this.#defaultStyle.id}.css-gg-video-icon {
                zoom:120%;
            }
            .${this.#defaultStyle.id}.video-description{            
                cursor: pointer;
                display: inline-flex;
                align-items: center;
            }
            .${this.#defaultStyle.id}.video-description::after{
                margin-left: 0.5em;
                content: ' ['attr(data-file_name)'] 'attr(data-open_status);
                font-size: small;
                color: #bdbdbd;
            }

            .${this.#defaultStyle.id}.video-contanier{
                width: fit-content;
                transition: height 0.5s ease-in-out;
                overflow: hidden;
                position: relative;
            }
            .${this.#defaultStyle.id}.video-contanier video{
                max-width: 100%;
                height: auto;
                aspect-ratio: attr(width) / attr(height);
            }
        `
        let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

    static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}
    

    #file;

    videoLoadEndCallback = (event) => {};

    video = Object.assign(document.createElement('video'), {
        loop: true,
        controls: true
    });

	constructor(dataset){
		super(Video, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});

		if( ! dataset && Object.entries(this.dataset).length == 0){
            this.#file = Video.selectedFile.cloneNode(true);
            Video.selectedFile.files = new DataTransfer().files;
            
            this.dataset.url = URL.createObjectURL(this.#file.files[0]);
            this.dataset.name = this.#file.files[0].name;
            this.dataset.lastModified = this.#file.files[0].lastModified;
            this.dataset.size = this.#file.files[0].size;
            this.dataset.content_type = this.#file.files[0].type;

            let url = URL.createObjectURL(this.#file.files[0], this.dataset.content_type);
            this.dataset.url = url;
            this.video.type = this.dataset.content_type;
            this.video.src = this.dataset.url;
            /*fetch(this.dataset.url).then(res=>res.blob()).then(blob => {
                const reader = new FileReader();
                reader.readAsDataURL(blob);
                reader.onloadend = () => {
                    this.dataset.base64 = reader.result;
                }
            })*/
            
        }else if(( ! this.dataset.url || this.dataset.url.startsWith('blob:file')) && this.dataset.base64){
            fetch(this.dataset.base64)
            .then(async res=>{
                return res.blob().then(blob=>{
                    let videoUrl = URL.createObjectURL(blob, res.headers.get('Content-Type'))
                    this.dataset.url = videoUrl;
                    this.video.src = this.dataset.url;
                })
            })
        }else if(Video.customVideoCallback && typeof Video.customVideoCallback == 'function'){
            Video.customVideoCallback(this);
        }else if(this.dataset.url){
            this.video.src = this.dataset.url;
        }

        if(! this.dataset.name){
            this.remove();
            throw new Error(`this file is undefined ${this.dataset.name}`);
        }

        this.attachShadow({ mode : 'open' });
        this.shadowRoot.append(Video.defaultStyle.cloneNode(true));

        this.createDefaultContent().then(({wrap, description, slot, aticle})=>{
            this.connectedChildAfterCallback = (addedList) => {
                aticle.append(...addedList);
            }
        });

        this.disconnectedAfterCallback = () => {
            if(this.dataset.url.startsWith('blob:file')){
                setTimeout(() => {
                    URL.revokeObjectURL(this.dataset.url);
                }, 1000 * 60 * 2)
            }
        }
	}

    createDefaultContent(){
        return new Promise(resolve=>{
            let wrap = Object.assign(document.createElement('div'),{

            });
            wrap.draggable = false

            this.shadowRoot.append(wrap);

            let videoContanier = Object.assign(document.createElement('div'),{
                className: `${Video.defaultStyle.id} video-contanier`
            });

            this.video.dataset.video_name = this.dataset.name

            videoContanier.append(this.video);
            this.video.onload = () => {
                if(
                    (this.file.length != 0 && video.canPlayType(this.file.files[0].type) == '') ||
                    (
                        this.dataset.name && 
                        this.video.canPlayType(
                            `video/${this.dataset.name.substring(this.dataset.name.lastIndexOf('.') + 1)}`
                        ) == ''
                    )
                ){
                    this.videoIsNotWorking();
                }
            }
            this.video.onloadeddata = () => {
                //videoContanier.style.height = window.getComputedStyle(video).height;
                /*let applyToolAfterSelection = window.getSelection(), range = applyToolAfterSelection.getRangeAt(0);
                let scrollTarget;
                if(range.endContainer.nodeType == Node.TEXT_NODE){
                    scrollTarget = range.endContainer.parentElement
                }else{
                    scrollTarget = range.endContainer;
                }
                scrollTarget.scrollIntoView({ behavior: "instant", block: "end", inline: "nearest" });
                */
                this.video.play();
                if(this.dataset.width){
                    this.video.width = this.dataset.width;
                }
            }
            this.video.onerror = () => {
                //videoContanier.style.height = window.getComputedStyle(video).height;
            }

            let {description, slot, aticle} = this.createDescription(this.video, videoContanier);

            this.connectedAfterOnlyOneCallback = () => {
                if(this.childNodes.length != 0 && this.childNodes[0]?.tagName != 'BR'){
                    aticle.append(...[...this.childNodes].filter(e=>e!=aticle));
                }
                wrap.replaceChildren(...[description,videoContanier].filter(e=>e != undefined));
                
                Video.videoBox.addVideoHoverEvent(this.video, this);
                if(this.nextSibling?.tagName == 'BR'){
                    this.nextSibling.remove()
                }

                resolve({wrap, description, slot, aticle})
            }
        })
    }

    /**
     * 
     * @param {HTMLVideoElement} image 
     * @param {HTMLDivElement} imageContanier 
     * @returns {HTMLDivElement}
     */
    createDescription(video, videoContanier){
        let description = Object.assign(document.createElement('div'), {
            className : `${Video.defaultStyle.id} video-description`
        });

        description.dataset.file_name = this.dataset.name
        description.dataset.open_status = this.dataset.open_status || '▼'
        videoContanier.style.height = this.dataset.height || 'auto'

        let {slot, aticle} = this.createSlot()
        
        if(slot){
            description.append(slot);
        }

        description.onclick = (event) => {

            if(description.dataset.open_status == '▼'){
                description.dataset.open_status = '▶'
                videoContanier.style.height = window.getComputedStyle(video).height;
                setTimeout(()=>{
                    videoContanier.style.height = '0px';
                    this.dataset.height = '0px';
                },100)

            }else{
                description.dataset.open_status = '▼';
                setTimeout(()=>{
                    videoContanier.style.height = window.getComputedStyle(video).height;
                    this.dataset.height = 'auto';
                },100)
                
                video.style.opacity = '';
                video.style.visibility = '';
            }

            this.dataset.open_status = description.dataset.open_status;
        }

        videoContanier.ontransitionend = () => {
            if(description.dataset.open_status == '▼'){
                videoContanier.style.height = 'auto';
            }else{
                video.style.opacity = 0;
                video.style.visibility = 'hidden';
            }
        }

        return {description, slot, aticle};
    }

    createSlot(){
        let aticle = document.createElement('div');
        
        aticle.contentEditable = 'false';
        aticle.draggable = 'false';

        //if(this.childNodes.length != 0 && this.childNodes[0]?.tagName != 'BR'){
            let randomId = Array.from(
                window.crypto.getRandomValues(new Uint32Array(16)),
                (e)=>e.toString(32).padStart(2, '0')
            ).join('');
            //aticle.append(...[...this.childNodes].map(e=>e.cloneNode(true)));
            aticle.append(...this.childNodes);
            aticle.slot = Video.descriptionName + '-' + randomId;
            this.append(aticle);
            
            let slot = Object.assign(document.createElement('slot'),{
                name: Video.descriptionName + '-' + randomId
            });

            return {slot, aticle};
        //}else{
        //    return undefined;
        //}
    }

    videoIsNotWorking(){
        alert(`${this.dataset.name}은 호환되지 않는 영상입니다.`);
        this.remove();
    }

    /**
     * @returns {HTMLInputElement}
     */
    get selectedFile(){
        return this.#file
    }
}

;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Resources.js




class Resources extends FreedomInterface {

	static toolHandler = new ToolHandler(this);

    static customResourcesCallback = () => {}; 

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-resources-style'
	});

    static slotName = 'free-will-editor-resources-description-slot';

    static #selectedFile = Object.assign(document.createElement('input'), {
        type: 'file'
    });

    static get selectedFile(){
        return this.#selectedFile;
    }

    static #resourcesCallback = () => {};

    static #sizeLimitOverCallback = () => {};

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-resources';
        //this.toolHandler.isInline = false;

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '',
            className: `${this.#defaultStyle.id}-button`,
            innerHTML: `
            <svg class="${this.#defaultStyle.id} css-gg-file-add-icon" width="0.9rem" height="0.9rem" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M10 18V16H8V14H10V12H12V14H14V16H12V18H10Z" fill="currentColor" />
                <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M6 2C4.34315 2 3 3.34315 3 5V19C3 20.6569 4.34315 22 6 22H18C19.6569 22 21 20.6569 21 19V9C21 5.13401 17.866 2 14 2H6ZM6 4H13V9H19V19C19 19.5523 18.5523 20 18 20H6C5.44772 20 5 19.5523 5 19V5C5 4.44772 5.44772 4 6 4ZM15 4.10002C16.6113 4.4271 17.9413 5.52906 18.584 7H15V4.10002Z"
                    fill="currentColor"
                />
            </svg>
            `,
            title: 'Resources'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
                this.#selectedFile.click();
                this.#selectedFile.onchange = ()=> {
                    //let url = URL.createObjectURL(this.#selectedFile.files[0])

                    this.toolHandler.toolButton.dataset.tool_status = 'active';
                }
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
            .${this.toolHandler.defaultClass} {
                position: relative;
            }
            .${this.#defaultStyle.id}.css-gg-file-add-icon {
                zoom:120%;
            }
            .${this.#defaultStyle.id}.resources-description{            
                cursor: pointer;
                display: inline-flex;
                align-items: center;
            }

            .${this.#defaultStyle.id}.resources-description::after{
                margin-left: 0.5em;
                content: ' ['attr(data-file_name)'] 'attr(data-open_status);
                font-size: small;
                color: #bdbdbd;
            }
            .${this.#defaultStyle.id}.resources-download-button, .${this.#defaultStyle.id}.resources-preview-button{
                margin-left: 1vw;
                border: solid 2px #e5e5e5;
                cursor: pointer;
                background: none;
            }
            .${this.#defaultStyle.id}.resources-contanier{
                width: auto;
                transition: height 0.5s ease-in-out;
                overflow: hidden;
                position: relative;
                margin-top: 0.7vh;
            }
            .${this.#defaultStyle.id}.resources-contanier object{
                width: 99%;
                height: auto;
                aspect-ratio: attr(width) / attr(height);
                border: solid 2px #efefef;
            }
            .${this.#defaultStyle.id}.resources-contanier object.unload{
                width: auto;
            }

        ` 
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

    static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

    static set resourcesCallback(callback){
        this.#resourcesCallback = callback;
    }
    static get resourcesCallback(){
        return this.#resourcesCallback;
    }

    static set sizeLimitOverCallback(callback){
        this.#sizeLimitOverCallback = callback;
    }
    static get sizeLimitOverCallback(){
        return this.#sizeLimitOverCallback;
    }

    #file;
    
    imgLoadEndCallback = (event) => {};

    resources = Object.assign(document.createElement('object'), {

    });

	constructor(dataset){
		super(Resources, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});
        
        if( ! dataset && Object.keys(this.dataset).length == 0){
            this.#file = Resources.selectedFile.cloneNode(true);
            Resources.selectedFile.files = new DataTransfer().files;

            this.dataset.url = URL.createObjectURL(this.#file.files[0]);
            this.dataset.name = this.#file.files[0].name;
            this.dataset.last_modified = this.#file.files[0].lastModified;
            this.dataset.size = this.#file.files[0].size;
            this.dataset.content_type = this.#file.files[0].type;

            let url = URL.createObjectURL(this.#file.files[0], this.dataset.content_type);
            this.dataset.url = url;
            /*fetch(this.dataset.url).then(res=>res.blob()).then(blob => {
                const reader = new FileReader();
                reader.readAsDataURL(blob);
                reader.onloadend = () => {
                    this.dataset.base64 = reader.result;
                }
            })*/
           
        }else if(Resources.customResourcesCallback && typeof Resources.customResourcesCallback == 'function'){
            Resources.customResourcesCallback(this);
        }else if(this.dataset.url){
        }
        this.resources.type = this.dataset.content_type;
        this.resources.data = this.dataset.url;
        this.resources.dataset.resources_name = this.dataset.name

        if(! this.dataset.name){
            this.remove();
            throw new Error(`this file is undefined ${this.dataset.name}`);
        }
        
        this.attachShadow({ mode : 'open' });
        this.shadowRoot.append(Resources.defaultStyle.cloneNode(true));
        
        this.createDefaultContent().then(({wrap, description, slot, aticle})=>{
            this.connectedChildAfterCallback = (addedList) => {
                aticle.append(...addedList);
            }
        });
        
        this.disconnectedAfterCallback = () => {
            this.resources.remove();
            if(this.dataset.url.startsWith('blob:file')){
                //setTimeout(() => {
                    //URL.revokeObjectURL(this.dataset.url);
                //}, 1000 * 60 * 2)
            }
        }

	}

    createDefaultContent(){
        return new Promise(resolve => {
            let wrap = Object.assign(document.createElement('div'),{

            });
            wrap.draggable = false

            this.shadowRoot.append(wrap);

            let resourcesContanier = Object.assign(document.createElement('div'),{
                className: `${Resources.defaultStyle.id} resources-contanier`
            });

            /*let resources = Object.assign(document.createElement('img'), {
                //src :`https://developer.mozilla.org/pimg/aHR0cHM6Ly9zLnprY2RuLm5ldC9BZHZlcnRpc2Vycy9iMGQ2NDQyZTkyYWM0ZDlhYjkwODFlMDRiYjZiY2YwOS5wbmc%3D.PJLnFds93tY9Ie%2BJ%2BaukmmFGR%2FvKdGU54UJJ27KTYSw%3D`
                //src: this.dataset.url
                //src: imgUrl
            });*/

            //if(this.file.files.length != 0){

            //}

            //resourcesContanier.append(this.resources);

            this.resources.onload = (event) => {
                if(this.dataset.width){
                    this.resources.width = this.dataset.width;
                }
                //console.log(this.resources.contentWindow);
                if( ! this.resources.contentWindow){
                    this.resources.classList.add('unload')
                }else{
                    this.resources.contentWindow.document.body.style.contentVisibility = 'auto'
                    this.resources.contentWindow.document.body.style.containIntrinsicSize = '1px 5000px'
                }

                Resources.resourcesCallback({status : 'load', resources : this.resources});
                /*let applyToolAfterSelection = window.getSelection(), range = applyToolAfterSelection.getRangeAt(0);
                let scrollTarget;
                if(range.endContainer.nodeType == Node.TEXT_NODE){
                    scrollTarget = range.endContainer.parentElement
                }else{
                    scrollTarget = range.endContainer;
                }
                scrollTarget.scrollIntoView({ behavior: "instant", block: "end", inline: "nearest" });
                */
            //this.imgLoadEndCallback();
                //resourcesContanier.style.height = window.getComputedStyle(resources).height;
            }
            this.resources.onerror = (event) => {
                //console.log(event);
                //resourcesContanier.style.height = window.getComputedStyle(resources).height;
                this.resources.dataset.error = '';
                if( ! this.resources.contentWindow){
                    this.resources.classList.add('unload')
                }
                Resources.resourcesCallback({status : 'error', resources : this.resources});
                
            }

            let {description, slot, aticle} = this.createDescription(this.resources, resourcesContanier);
            let downloadButton = Object.assign(document.createElement('button'), {
                className: `${Resources.defaultStyle.id} resources-download-button`,
                innerHTML : `<b>Download</b>`,
                onclick : () => {
                    fetch(this.resources.data).then(res=>res.blob()).then(blob => {
                        let url = URL.createObjectURL(blob, this.dataset.content_type);
                        let a = document.createElement('a');
                        a.href = url;
                        a.download = this.dataset.name;
                        a.click();
                        a.remove();
                        setTimeout(() => {
                            URL.revokeObjectURL(url);
                        }, 1000 * 60 * 5)
                    })
                }
            });
            let previewButton = Object.assign(document.createElement('button'), {
                className: `${Resources.defaultStyle.id} resources-preview-button`,
                innerHTML : `<b>Preview</b>`,
                onclick : () => {
                    resourcesContanier.append(this.resources);
                }
            });

            this.connectedAfterOnlyOneCallback = () => {
                if(this.childNodes.length != 0 && this.childNodes[0]?.tagName != 'BR'){
                    aticle.append(...[...this.childNodes].filter(e=>e!=aticle));
                }
                wrap.replaceChildren(...[description,downloadButton,previewButton,resourcesContanier].filter(e=>e != undefined));
                
                if(this.nextSibling?.tagName == 'BR'){
                    this.nextSibling.remove()
                }

                resolve({wrap, description, slot, aticle})
            }
        })
    }

    /**
     * 
     * @param {HTMLObjectElement} resources 
     * @param {HTMLDivElement} resourcesContanier 
     * @returns 
     */
    createDescription(resources, resourcesContanier){
        let description = Object.assign(document.createElement('div'),{
            className: `${Resources.defaultStyle.id} resources-description`
        });

        description.dataset.file_name = this.dataset.name
        description.dataset.open_status = this.dataset.open_status || '▼'
        resourcesContanier.style.height = this.dataset.height || 'auto'
        
        description.dataset.open_status = '▼';
        
        let {slot, aticle} = this.createSlot();
        
        description.append(slot)

        description.onclick = (event) => {
            /*if(event.composedPath().some(e=> e== downloadButton)){
                return;
            }*/
            if(description.dataset.open_status == '▼'){
                description.dataset.open_status = '▶'
                resourcesContanier.style.height = window.getComputedStyle(resources).height;
                setTimeout(()=>{
                    resourcesContanier.style.height = '0px';
                    this.dataset.height = '0px';
                },100)

            }else{
                description.dataset.open_status = '▼';
                setTimeout(()=>{
                    resourcesContanier.style.height = window.getComputedStyle(resources).height;
                    this.dataset.height = 'auto';
                },100)
                
                resources.style.opacity = '';
                resources.style.visibility = '';

            }
            this.dataset.open_status = description.dataset.open_status;
        }

        resourcesContanier.ontransitionend = () => {
            if(description.dataset.open_status == '▼'){
                resourcesContanier.style.height = 'auto';
            }else{
                resources.style.opacity = 0;
                resources.style.visibility = 'hidden';
            }
        }

        return {description, slot, aticle};
    }

    /**
     * 
     * @returns {HTMLSlotElement}
     */
    createSlot(){
        let aticle = document.createElement('div');
        
        aticle.contentEditable = 'false';
        aticle.draggable = 'false'; 

        //if(this.childNodes.length != 0 && this.childNodes[0]?.tagName != 'BR'){
            let randomId = Array.from(
                window.crypto.getRandomValues(new Uint32Array(16)),
                (e)=>e.toString(32).padStart(2, '0')
            ).join('');
            //aticle.append(...[...this.childNodes].map(e=>e.cloneNode(true)));
            aticle.append(...this.childNodes);
            aticle.slot = Resources.slotName + '-' + randomId
            this.append(aticle);
            
            let slot = Object.assign(document.createElement('slot'),{
                name: Resources.slotName + '-' + randomId
            });
            return {slot, aticle};
        //}else{
        //   return undefined
        //}

    }
    /**
     * @returns {HTMLInputElement}
     */
    get selectedFile(){
        return this.#file
    }
    
    set resourcesUrl(url){
        let clone = this.resources.cloneNode(true);
        clone.data = url;
        this.resources.after(clone);
        this.resources.remove();
        this.resources = clone;
        clone.onload = () => {
            if(this.resources == clone) return;
            if(this.dataset.width){
                clone.width = this.dataset.width;
            }
            if( ! clone.contentWindow){
                clone.classList.add('unload')
            }
        }
        clone.onerror = () => {
            clone.dataset.error = '';
            if( ! clone.contentWindow){
                clone.classList.add('unload')
            }
        }
    }
    get resourcesUrl(){
        this.resources.data;
    }
}

;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Code.js



class Code extends FreedomInterface {
	//static extendsElement = 'strong';
	//static defaultClass = 'line';
	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-code-style'
	});

	static{
		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-code';
		this.toolHandler.isInline = false;

		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            textContent: '',
            className: `${this.#defaultStyle.id}-button`,
            innerHTML: `
            <svg class="${this.#defaultStyle.id} css-gg-code-icon"
            width="0.9rem"
            height="0.9rem"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            >
                <path
                d="M13.325 3.05011L8.66741 20.4323L10.5993 20.9499L15.2568 3.56775L13.325 3.05011Z"
                fill="currentColor"
                />
                <path
                d="M7.61197 18.3608L8.97136 16.9124L8.97086 16.8933L3.87657 12.1121L8.66699 7.00798L7.20868 5.63928L1.04956 12.2017L7.61197 18.3608Z"
                fill="currentColor"
                />
                <path
                d="M16.388 18.3608L15.0286 16.9124L15.0291 16.8933L20.1234 12.1121L15.333 7.00798L16.7913 5.63928L22.9504 12.2017L16.388 18.3608Z"
                fill="currentColor"
                />
            </svg>
            `,
            title: 'Code Block'
        });

		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else{
				this.toolHandler.toolButton.dataset.tool_status = 'active';
			}
		}
	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
            .${this.#defaultStyle.id}-button{
            }
            .${this.#defaultStyle.id}.css-gg-code-icon{
                zoom:120%;
            }
			.${this.toolHandler.defaultClass} {
                display: block;
                background-color: #e7e7e7;
                margin-inline: 0.5em;
                border: solid 1px #d1d1d1;
                border-radius: 4px;
                white-space: pre-wrap;
                font-family: monospace;
                box-shadow: 0px 0px 3px 0px #d1d1d1;
                padding: 0.5em 1em 0.5em 1em;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	parentLine;

	constructor(dataset){
		super(Code, dataset, {deleteOption : FreedomInterface.DeleteOption.EMPTY_CONTENT_IS_NOT_DELETE});
        /*
		super.connectedAfterOnlyOneCallback = () => {
			this.dataset.index = Code.toolHandler.connectedFriends.length;
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}else{
				nextLine.line.lookAtMe();
			}
		}

        super.disconnectedChildAfterCallback = () => {
			let nextLine = this.parentEditor.getNextLine(this.parentLine);
			if( ! nextLine){
				this.parentEditor.createLine();
			}
        }
        */
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/component/HyperlinkBox.js
class HyperlinkBox{
	#style = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-hyperlink-box'
	});

	#hyperlinkBox = Object.assign(document.createElement('div'),{
		className: 'hyperlink-box-wrap'
	});

	#hyperlinkBoxVw = 20;

	#urlInputText = Object.assign(document.createElement('input'), {
        autocomplete: 'off',
        placeholder: 'Please enter your link ',
        type: 'url',
        name: 'hyperlink-url-input',
        className: 'hyperlink-url-input',
		required: false,
        /*
		oninput: (event) => this.#searchInputTextEvent(event),
        onkeyup: (event) => {
            if(event.key == 'Enter' && this.#searchInputText.value != ''){
                let item = this.#fontSizeBoxContainer.querySelector(`[data-size="${this.#searchInputText.value}"]`);
                this.#apply(item, event);
            }
        },
		*/
    });

	#invalidUrlMessage = Object.assign(document.createElement('span'), {

	})

	#applyCallback = () => {}
	
    #lastSelectionRange;

	#lastUrl;

	constructor(){
		let style = document.querySelector(`#${this.#style.id}`);
        if(! style){
            document.head.append(this.createStyle());
        }else{
            this.#style = style;
        }
		let urlInputTextWrapper = Object.assign(document.createElement('div'), {
			//id: 'url-input-text-wrapper',
			className: 'url-input-text-wrapper',
			/*onsubmit : (event) => {
				event.preventDefault();
				applyButton.click();
			}*/
		})
		urlInputTextWrapper.append(this.#urlInputText);
		this.#urlInputText.oninput = (event) => {
			if(this.#invalidUrlMessage.isConnected){
				this.#invalidUrlMessage.remove();
			}
		}
		this.#urlInputText.onkeydown = (event) => {
			if(event.key == 'Enter'){
				event.preventDefault();
				applyButton.onclick();
			}

		}

		let cancelButton = Object.assign(document.createElement('button'), {
			className: 'cancel-button',
			type: 'button',
			textContent: 'cancel'
		})
		cancelButton.onclick = () => {
			this.close();
		}
		let applyButton = Object.assign(document.createElement('button'), {
			className: 'apply-button',
			type: 'button',
			textContent: 'apply'
		})
		applyButton.onclick = (event) => {
			if( ! this.#urlInputText.checkValidity()){
				let prevValue = this.#urlInputText.value
				this.#urlInputText.value = `https://${this.#urlInputText.value}`;
				if(! this.#urlInputText.checkValidity()){
					this.#urlInputText.value = prevValue;
					return;
				}
			}
			this.lastUrl = this.#urlInputText.value;
			this.#urlInputText.value = '';
			if(this.#lastSelectionRange){
				let selection = window.getSelection();
				selection.removeAllRanges();
				selection.addRange(this.#lastSelectionRange);
			}
			this.applyCallback(event);
		}
		let buttonWrapeer = Object.assign(document.createElement('div'), {
			className: 'button-wrap',
		})
		buttonWrapeer.append(cancelButton, applyButton);
		this.#hyperlinkBox.append(urlInputTextWrapper, buttonWrapeer)
	}

    async open(){
		let selection = window.getSelection();
		if(selection.rangeCount != 0 && selection.isCollapsed == false){
			let range = selection.getRangeAt(0);
			this.#lastSelectionRange = range.cloneRange();
		}
		document.body.append(this.#hyperlinkBox);
		this.#urlInputText.focus();
    }

    close(){
        this.#hyperlinkBox.remove();
    }
    
	set applyCallback(applyCallback){
        this.#applyCallback = applyCallback;
    }

    get applyCallback(){
        return this.#applyCallback;
    }

	get hyperlinkBox(){
		return this.#hyperlinkBox;
	}

	createStyle(){
		this.#style.textContent = `
			.hyperlink-box-wrap{
				background: #343434;
				position: fixed;
				padding: 0.9%;
				width: ${this.#hyperlinkBoxVw}vw;
				height: auto;
				color: white;
				min-width: 100px;
				overflow-y: auto;
				-webkit-user-select: none;
				-moz-user-select: none;
				-ms-user-select: none;
				user-select: none;
				overflow-x: hidden;
				display: grid;
				gap: 1vh;
				align-content: center;
				justify-content: space-around;
				align-items: center;
				justify-items: center;
                z-index: 999;
			}
			.hyperlink-box-wrap .url-input-text-wrapper{
				width: inherit;
			}
			.hyperlink-box-wrap .url-input-text-wrapper .hyperlink-url-input{
				width: -webkit-fill-available;
				background-color: #ffffff00;
				color: #b9b9b9;
				border-bottom: solid 1px #4c4c4c;
				border-left: solid 1px #4c4c4c;
			}
			.hyperlink-box-wrap .button-wrap{
				display: flex;
				justify-content: space-around;
				width: inherit;
			}
			.hyperlink-box-wrap .button-wrap .apply-button,
			.hyperlink-box-wrap .button-wrap .cancel-button {
				background: none;
				border: revert;
				color: #b9b9b9;
				border-color: #464646;
			}
		`
		return this.#style;
	}
}
;// CONCATENATED MODULE: ./view/js/handler/editor/tools/Hyperlink.js




class Hyperlink extends FreedomInterface {
	static toolHandler = new ToolHandler(this);

	static #defaultStyle = Object.assign(document.createElement('style'), {
		id: 'free-will-editor-link-style'
	});

    static hyperlinkBox;

	static{

		this.toolHandler.extendsElement = '';
		this.toolHandler.defaultClass = 'free-will-editor-link';
		
		this.toolHandler.toolButton = Object.assign(document.createElement('button'), {
            className: `${this.#defaultStyle.id}-button`,
			innerHTML: `
				<svg class="${this.#defaultStyle.id} css-gg-link-icon"
				width="0.9rem"
				height="0.9rem"
				viewBox="0 0 24 24"
				fill="none"
				xmlns="http://www.w3.org/2000/svg"
				>
					<path
						d="M14.8284 12L16.2426 13.4142L19.071 10.5858C20.6331 9.02365 20.6331 6.49099 19.071 4.9289C17.509 3.3668 14.9763 3.3668 13.4142 4.9289L10.5858 7.75732L12 9.17154L14.8284 6.34311C15.6095 5.56206 16.8758 5.56206 17.6568 6.34311C18.4379 7.12416 18.4379 8.39049 17.6568 9.17154L14.8284 12Z"
						fill="currentColor"
					/>
					<path
						d="M12 14.8285L13.4142 16.2427L10.5858 19.0711C9.02372 20.6332 6.49106 20.6332 4.92896 19.0711C3.36686 17.509 3.36686 14.9764 4.92896 13.4143L7.75739 10.5858L9.1716 12L6.34317 14.8285C5.56212 15.6095 5.56212 16.8758 6.34317 17.6569C7.12422 18.4379 8.39055 18.4379 9.1716 17.6569L12 14.8285Z"
						fill="currentColor"
					/>
					<path
						d="M14.8285 10.5857C15.219 10.1952 15.219 9.56199 14.8285 9.17147C14.4379 8.78094 13.8048 8.78094 13.4142 9.17147L9.1716 13.4141C8.78107 13.8046 8.78107 14.4378 9.1716 14.8283C9.56212 15.2188 10.1953 15.2188 10.5858 14.8283L14.8285 10.5857Z"
						fill="currentColor"
					/>
				</svg>
			`,
			title: 'Hyperlink'
        });
		
		this.hyperlinkBox = new HyperlinkBox();
		
		this.toolHandler.toolButton.onclick = ()=>{
			if(this.toolHandler.toolButton.dataset.tool_status == 'active' || this.toolHandler.toolButton.dataset.tool_status == 'connected'){
				this.toolHandler.toolButton.dataset.tool_status = 'cancel';
			}else if(this.hyperlinkBox.hyperlinkBox.isConnected){
				this.hyperlinkBox.close();
			}else{
				this.hyperlinkBox.open().then(()=>{
					this.toolHandler.processingElementPosition(this.hyperlinkBox.hyperlinkBox);
				});
			}
		}

		document.addEventListener("scroll", () => {
			if(this.hyperlinkBox.hyperlinkBox.isConnected){
				this.toolHandler.processingElementPosition(this.hyperlinkBox.hyperlinkBox);
			}
		});
        window.addEventListener('resize', (event) => {
            if(this.hyperlinkBox.hyperlinkBox.isConnected){
				this.toolHandler.processingElementPosition(this.hyperlinkBox.hyperlinkBox);
            }
		})

		this.hyperlinkBox.applyCallback = (event) => {
			this.toolHandler.toolButton.dataset.tool_status = 'active'
			this.hyperlinkBox.close();
		}

		super.outClickElementListener(this.hyperlinkBox.hyperlinkBox, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.hyperlinkBox.hyperlinkBox.isConnected && ! super.isMouseInnerElement(this.toolHandler.toolButton)){
				this.hyperlinkBox.close();
			}
		});
		

	}

	static createDefaultStyle(){
		this.#defaultStyle.textContent = `
			.${this.#defaultStyle.id}-button{
			}
			.${this.#defaultStyle.id}.css-gg-link-icon{
				zoom:120%;
			}
			.${this.toolHandler.defaultClass} {
				color: -webkit-link;
				cursor: pointer;
				text-decoration: underline;
				display: grid;
			}
			.${this.toolHandler.defaultClass} > [data-hyperlink_child="${Hyperlink.toolHandler.defaultClass}-child"]{
				all: initial;
				display: inline-block;
				margin-left: 1em;
				padding-left: 1em;
				border-left: 5px solid #d7d7db;
				width: 95%;
				float: left;
			}
			.${this.toolHandler.defaultClass} > [data-hyperlink_child="${Hyperlink.toolHandler.defaultClass}-child"] > *{
				font-size: 14px;
			}
			.${this.toolHandler.defaultClass} > [data-hyperlink_child="${Hyperlink.toolHandler.defaultClass}-child"] img{
				max-width: 100%;
                height: auto;
                aspect-ratio: attr(width) / attr(height);
			}
			.${this.toolHandler.defaultClass}-preview-url{
				position: fixed;
				z-index: 9999;
				width: 50vw;
				height: 50vh;
			}
		`
		let defaultStyle = document.querySelector(`#${this.#defaultStyle.id}`);
        if(! defaultStyle){
            document.head.append(this.#defaultStyle);
        }else{
            this.#defaultStyle?.remove();
            this.#defaultStyle = defaultStyle;
            document.head.append(this.#defaultStyle);
        }
		return this.#defaultStyle;
	}

	static get defaultStyle(){
        return this.#defaultStyle;
    }

    static set defaultStyle(style){
        this.#defaultStyle.textContent = style;
    }

	static set insertDefaultStyle(style){
		this.#defaultStyle.sheet.insertRule(style);
	}

	#aTag = Object.assign(document.createElement('a'), {
		target: '_blank'
	});

	/*
	#previewUrl = Object.assign(document.createElement('iframe'), {
		className: `${Hyperlink.toolHandler.defaultClass}-preview-url`,
		//referrerpolicy: 'origin',
		//allow: 'Permissions-Policy',
		//width: 100,
		//height: 100,
	});
	*/
	/*HyperlinkChild = class HyperlinkChild extends HTMLElement{
		constructor(){
			super();
			this.className = `${Hyperlink.toolHandler.defaultClass}-child`;
			this.attachShadow({ mode : 'open' });
			this.shadowRoot.append(Hyperlink.defaultStyle.cloneNode(true));
		}
	};*/

	//#hyperlinkChild;
	constructor(dataset){
		super(Hyperlink, dataset);
		/*
		if( ! window.customElements.get(`${Hyperlink.toolHandler.defaultClass}-child`)){
			window.customElements.define(`${Hyperlink.toolHandler.defaultClass}-child`, this.HyperlinkChild);
		}
		
		this.#hyperlinkChild = new this.HyperlinkChild();
		
		*/
		let getUrlMetadataPromise;

		if( ! dataset && Object.keys(this.dataset).length == 0){
			this.dataset.href = Hyperlink.hyperlinkBox.lastUrl;
			getUrlMetadataPromise = fetch(this.dataset.href, {
				mode: 'no-cors',
			}).then(response => {
				return response.text();
			}).then(htmlText => {
				let dom = new DOMParser().parseFromString(htmlText, 'text/html');
				this.dataset.title = dom.querySelector('meta[name="og:title"]')?.getAttribute('content') 
					|| dom.querySelector('meta[name="twitter:title"]')?.getAttribute('content')
					|| dom.querySelector('title')?.textContent
					|| '';

				this.dataset.description = dom.querySelector('meta[name="description"]')?.getAttribute('content') 
					|| dom.querySelector('meta[name="og:description"]')?.getAttribute('content') 
					|| dom.querySelector('meta[name="twitter:description"]')?.getAttribute('content')
					|| ''
	
				this.dataset.image = dom.querySelector('meta[name="og:image"]')?.getAttribute('content')
					|| dom.querySelector('meta[name="twitter:image"]')?.getAttribute('content')
					|| ''

	
				this.dataset.favicon = dom.querySelector('link[rel="icon"]')?.getAttribute('href') || '';

				this.dataset.siteName = new URL(this.dataset.href).hostname;
	
				let p = document.createElement('p')
				//this.parentEditor.createLine();
				//this.#hyperlinkChild.shadowRoot.append(line);
				//this.append(line);
				return p;
			}).catch(error=>{
				console.error(error);
				//this.style.color = 'red';
				//this.title = 'unknown site';
				return undefined
			});
        }else{
			getUrlMetadataPromise = Promise.resolve().then(()=>{
				//let line = this.parentEditor.createLine();
				//line.contenteditable = false;
				//this.#hyperlinkChild.shadowRoot.append(line);
				//this.append(line);
				//let p = document.createElement('p')
				//return p
			});
		}
		this.#aTag.href = this.dataset.href
		super.connectedAfterOnlyOneCallback = () => {
			getUrlMetadataPromise.then(p => {
				if(! p){
					p = this.querySelector(`[data-hyperlink_child="${Hyperlink.toolHandler.defaultClass}-child"]`);
					if( ! p){
						return;
					}
				}
				let title;
				if(this.dataset.title != ''){
					title = Object.assign(document.createElement('p'), {
						textContent: this.dataset.title
					})
					title.style.fontWeight = 'bold';
				}
				let description;
				if(this.dataset.description != ''){
					description = Object.assign(document.createElement('p'),{
						textContent: this.dataset.description
					})
				}
				let image;
				if(this.dataset.image != ''){
					image = Object.assign(document.createElement('p'),{
						innerHTML: `
							<img data-image src="${this.dataset.image}"/>
						`
					});
				}
				let favicon;
				if(this.dataset.favicon != ''){
					favicon = Object.assign(document.createElement('span'), {
						innerHTML: `
							<img data-favicon src="${this.dataset.favicon}" style="width: 1.1em; vertical-align: text-bottom;"/>
						`
					})
				}
				let siteName = Object.assign(document.createElement('span'), {
					textContent: this.dataset.siteName
				});
				siteName.style.fontSize = '12px';
				siteName.style.fontWeight = 'bold';
				//this.#hyperlinkChild.shadowRoot.append(line);
				this.append(p);
				p.dataset.hyperlink_child = `${Hyperlink.toolHandler.defaultClass}-child`;
				p.contenteditable = false;
				p.replaceChildren(...[
					favicon, siteName,
					title,
					description,
					image
				].filter(e=>e!=undefined))

				this.onclick = (event) => {
					if( ! event.composedPath().some(e=>e==p)){
						this.#aTag.click();
					}
				}
			}) 
		}


		/*
		this.onmouseenter = () => {
			if(this.#previewUrl.src != this.dataset.href){
				this.#previewUrl.src = this.dataset.href;
			}
			if( ! this.#previewUrl.isConnected){
				document.body.append(this.#previewUrl);
				//this.append(this.#previewUrl);
			}
			Hyperlink.toolHandler.processingElementPosition(this.#previewUrl, this);
		}		

		FreedomInterface.outClickElementListener(this.#previewUrl, ({oldEvent, newEvent, isMouseOut})=>{
			if(isMouseOut && this.#previewUrl.isConnected && ! FreedomInterface.isMouseInnerElement(this)){
				this.#previewUrl.remove();
			}
		})
		document.addEventListener("scroll", () => {
			if(this.#previewUrl.isConnected){
				Hyperlink.toolHandler.processingElementPosition(this.#previewUrl, this);
			}
		});
		window.addEventListener('resize', (event) => {
			if(this.#previewUrl.isConnected){
				Hyperlink.toolHandler.processingElementPosition(this.#previewUrl, this);
			}
		})
		*/
	}

}
;// CONCATENATED MODULE: ./view/js/handler/editor/FreeWillEditor.js






















class FreeWillEditor extends FreeWiilHandler {
	
	static componentsMap = {};
	static toolsMap = {};

	static LineBreakMode = class LineBreakMode{
		static #LineBreakModeEnum = class LineBreakModeEnum{
			value;
			constructor(value){
				this.value = value;
				Object.freeze(this);
			}
		}
		static NO_CHANGE = new this.#LineBreakModeEnum(1)
		static NEXT_LINE_FIRST = new this.#LineBreakModeEnum(2);
		static NEXT_LINE_LAST = new this.#LineBreakModeEnum(3);
	}

	#isLoaded = false;
	components;
	tools;
	#placeholder;
	#undoManager;
	isDefaultStyle = true;
	#toolButtonObserver = new MutationObserver( (mutationList, observer) => {
		mutationList.forEach((mutation) => {
			//if(mutation.oldValue == mutation.mutation.target.dataset.tool_status){
				// 동일한 동작이 수행되지 않도록 추가 2023 05 25
			//	return;
			//}
			if(this.contentEditable == 'false' || (mutation.target.dataset.tool_status == 'connected' &&  mutation.oldValue == mutation.target.dataset.tool_status == 'connected')){
				//observer.disconnect();
				return;
			}
			let selection = window.getSelection();
			/*if( ! selection.containsNode(this, true)){
				console.log(222)
				return;
			}*/
			let focusNode = selection.focusNode;
			let Tool = mutation.target.__Tool;
			if(mutation.target.dataset.tool_status == 'active' && mutation.oldValue != 'active' && Tool.prototype.isPrototypeOf(focusNode.parentElement) == false){
				this.#renderingTools(Tool);
			}else if(mutation.target.dataset.tool_status == 'cancel' && mutation.oldValue != 'cancel'){// && window.getSelection().isCollapsed == false){
				this.#removerTools(Tool);
			}
		});
	});
	constructor(
		tools = [
			Strong,
			Color,
			Background,
			Strikethrough,
			Underline,
			FontFamily,
			Quote,
			NumericPoint,
			BulletPoint,
			Sort,
			FontSize,
			Italic,
			Image,
			Video,
			Resources,
			Code,
			Hyperlink,
		],
		{isDefaultStyle = true} = {}
	){
		super();
		if(isDefaultStyle){
			FreeWiilHandler.createDefaultStyle();
		}
		new Promise(resolve => {
			this.isDefaultStyle = isDefaultStyle;
			this.components = {
				'free-will-editor-line' : Line
			};
			this.tools = tools;
			this.classList.add('free-will-editor');

			Object.entries(this.components).forEach( ([className, Component]) => {
				if(className.includes(' ')){
					throw new DOMException(`The token provided ('${className}') contains HTML space characters, which are not valid in tokens.`);
				}
				if(FreeWillEditor.componentsMap[Component.name]){
					return;
				}
				Component.toolHandler.defaultClass = className;
				if( ! window.customElements.get(className)){
					window.customElements.define(className, Component, Component.toolHandler.extendsElement && Component.toolHandler.extendsElement != '' ? {extends:Component.toolHandler.extendsElement} : undefined);
				}	
				FreeWillEditor.componentsMap[Component.name] = Component;
			});
			
			this.tools.forEach( (Tool) => {
				if(FreeWillEditor.toolsMap[Tool.name]){
					return;
				}
				if(this.isDefaultStyle){
					Tool.createDefaultStyle();
				}
				Tool.toolHandler.toolButton.__Tool = Tool;
				// attribute에 value가 없어서 oldvalue가 ''이 나옵니다.
				// oldvalue로 구분할 수 있게 합시다.
				this.#toolButtonObserver.observe(Tool.toolHandler.toolButton, {
					attributeFilter:['data-tool_status'],
					attributeOldValue:true
				})
				
				if( ! window.customElements.get(Tool.toolHandler.defaultClass)){
					window.customElements.define(Tool.toolHandler.defaultClass, Tool, Tool.toolHandler.extendsElement && Tool.toolHandler.extendsElement != '' ? {extends:Tool.toolHandler.extendsElement} : undefined);
				}
				FreeWillEditor.toolsMap[Tool.name] = Tool;
			})
			
			

			let observer = new MutationObserver( (mutationList, observer) => {
				mutationList.forEach((mutation) => {
					//if(this.contentEditable == 'false'){
					//	return;
					//}
					
					if(this.childElementCount == 0){
						this.startFirstLine();
					}
					if( ! this.firstElementChild.line){
						new Line(this.firstElementChild.line);
					}
					
					this.querySelectorAll('[data-placeholder]').forEach(async e => {
						e.removeAttribute('data-placeholder')
					})
					if(this.isEmpty){
						this.placeholder = this.#placeholder;
						//this.firstElementChild.dataset.placeholder = this.#placeholder
					}else{
						this.firstElementChild.setAttribute('data-placeholder', '');
					}
					//console.log(mutation.target);
					mutation.addedNodes.forEach(async element=>{
						new Promise(resolve=>{
							if(element.nodeType != Node.ELEMENT_NODE) return;
							/*
							let sty = window.getComputedStyle(element);
							
							if(sty.visibility == 'hidden' || sty.opacity == 0){
								return;
							}
							*/
							if(element.classList.contains(Line.toolHandler.defaultClass)){
								if( ! element.line){
									new Line(element);
								}
								if(element.innerText.length == 0){
									element.innerText = '\n';
									window.getSelection().setPosition(element, 1)
									element.focus();
								}
							}
							resolve();
						})
					})
					/*mutation.removedNodes.forEach(element=>{

					})*/
				})
			});
			observer.observe(this, {
				childList:true,
				subtree: true
			})
			resolve();
		});
	}

	connectedCallback(){
		this.#undoManager = new UndoManager(this);
		if( ! this.#isLoaded){
            this.#isLoaded = true;
			if(this.contentEditable == 'inherit' || this.contentEditable == true){
				this.contentEditable = true;
				this.tabIndex = 1;
				this.focus()
				if(this.isEmpty){
					this.startFirstLine();
				}
			}
		}
	}
	disconnectedCallback(){
		this.#undoManager = null;
        this.#isLoaded = false;
		//this.contentEditable = false;
		this.#toolButtonObserver.disconnect();
    }

	startFirstLine(){
		let lineElement = super.createLine();
		lineElement.line.isFirstLine = true;
		this.placeholder = this.#placeholder;
		return lineElement;
	}

	#removeAfterSelectionMove(targetElement){
		let selection = document.getSelection()
		let range =  new Range();//document.createRange()
		let targetLine = Line.getLine(targetElement);
		range.setStartAfter(targetLine)
		range.setEnd(targetLine, targetElement.textContent.length - 1);
		selection.removeAllRanges()
		selection.addRange(range) 
	}

	#addAfterSelectionMove(targetElement){
		let selection = document.getSelection()
		let range =  new Range();//document.createRange()
		if( ! targetElement){
		//	return;
		}
		if(targetElement && targetElement.nodeType == Node.ELEMENT_NODE && targetElement.textContent.length == 0){
			//let emptyElement = document.createTextNode('\u200B')
			//targetElement.textContent = '\u200B'; //.append(emptyElement)
		}
		
		range.selectNodeContents(targetElement)
		range.setStart(targetElement, targetElement.length);
		range.setEnd(targetElement, targetElement.length);
		selection.removeAllRanges()
		selection.addRange(range)
		targetElement.removeAttribute('tabIndex');
		//cursor
		/*
		let observer = new MutationObserver( (mutationList, observer) => {
			mutationList.forEach((mutation) => {
				if(mutation.target.textContent.includes('\u200B')){
					if(targetElement.toolHandler && targetElement.toolHandler.toolButton ? true: false){
						targetElement.toolHandler.toolButton.setAttribute('data-is_alive', '');
					}
					mutation.target.textContent = mutation.target.textContent.replace('\u200B', '');
					console.log('146 :: targetElement.isConnected',targetElement.isConnected);
					console.log('147 :: targetElement',targetElement);
					if(targetElement.isConnected){
						selection.modify('move', 'forward', 'line')
					}
					targetElement.removeAttribute('tabIndex');
					observer.disconnect();
				}else{
					targetElement.removeAttribute('tabIndex');
					observer.disconnect();
				}
			});
		})

		observer.observe(targetElement, {
			characterData: true,
			characterDataOldValue: true,
			childList:true,
			subtree: true
		})
		*/
	}

	#renderingTools(TargetTool){
		let selection = window.getSelection();
		let {isCollapsed, anchorNode, focusNode} = selection;
		// 범위 선택 x인 경우 넘어가기
		if(isCollapsed && TargetTool.toolHandler.isInline){
			return;
		}
		super.getLineRange(selection)
		.then(({startLine, endLine}) => { 
			if( ! startLine){
				startLine = endLine
			}
			if( ! startLine.line){
				new Line(startLine);
			}
			if( ! endLine.line){
				new Line(endLine);
			}
			return startLine.line.applyTool(TargetTool, selection.getRangeAt(0), endLine)
		})
		.then(lastApplyTool=> {
			if( ! this.#undoManager){
				this.#undoManager = new UndoManager(this);
			}
			this.#undoManager.addUndoRedo(true);
			let applyToolAfterSelection = window.getSelection(), range = applyToolAfterSelection.getRangeAt(0);
			let scrollTarget;
			if(range.endContainer.nodeType == Node.TEXT_NODE){
				scrollTarget = range.endContainer.parentElement
			}else{
				scrollTarget = range.endContainer;
			}
			scrollTarget.scrollIntoView({ behavior: "instant", block: "end", inline: "nearest" });
			applyToolAfterSelection.setPosition(scrollTarget, scrollTarget.childNodes.length - 1);
			//selection.setPosition(lastApplyTool, 0)
		})
		/*
		.then(tool=>{
			this.querySelectorAll(`.${TargetTool.toolHandler.defaultClass}`).forEach(async e =>{
				await new Promise(resolve=>{
					resolve();
				})
			})
			//this.#addAfterSelectionMove(tool);
		});
		*/
	}

	#removerTools(TargetTool){
		let selection = window.getSelection();
		let {isCollapsed, anchorNode, focusNode} = selection;
		
		// 범위 선택 x인 경우 넘어가기
		if(isCollapsed && TargetTool.toolHandler.isInline){
			return;
		}
		super.getLineRange(selection).then(({startLine, endLine})=> {
			//console.log(endLine);
			if( ! startLine){
				startLine = endLine
			}
			if( ! startLine.line){
				new Line(startLine);
			}
			if( ! endLine.line){
				new Line(endLine);
			}
			startLine.line.cancelTool(TargetTool, selection, endLine)
			.then(()=>{
				if( ! this.#undoManager){
					this.#undoManager = new UndoManager(this);
				}
				this.#undoManager.addUndoRedo(true);
				window.getSelection().setPosition(startLine, startLine.childElementCount);
				/*[...this.children]
				.filter(e=>e.classList.contains(`${Line.toolHandler.defaultClass}`))
				.forEach(lineElement=>{
					if(lineElement.line.isLineEmpty()){
						//lineElement.line = undefined;
						//lineElement.remove();
					}
				})*/
			});
		})
	}
	
	async lineBreak(LineBreakMode = FreeWillEditor.LineBreakMode.NEXT_LINE_FIRST){
		let selection = window.getSelection();
		return super.getLineRange(selection)
		.then(({startLine, endLine}) => { 
			if( ! startLine.line){
				new Line(startLine);
			}
			if( ! endLine.line){
				new Line(endLine);
			}
			let range = selection.getRangeAt(0);
			let {startOffset, endOffset, startContainer,endContainer} = range;
			let lastItem = startLine.lastChild
			let lastItemEndOffset = lastItem.nodeType == Node.TEXT_NODE ? lastItem.length : lastItem.childNodes.length;
			range.setStart(startContainer, startOffset);
			range.setEnd(lastItem, lastItemEndOffset);
			let lineElement = super.createLine();
			lineElement.append(range.extractContents());
			startLine.after(lineElement);
			if(LineBreakMode == FreeWillEditor.LineBreakMode.NEXT_LINE_FIRST){
				window.getSelection().setPosition(lineElement, 0);
			}else if(LineBreakMode == FreeWillEditor.LineBreakMode.NEXT_LINE_LAST){
				lineElement.line.lookAtMe();
			}

		})
	}

	async getLowDoseJSON(targetElement = this, {afterCallback = (json)=> {}} = {}){
		console.log(targetElement)
		return Promise.all([...targetElement.childNodes]
			.map(async (node, index)=>{
				return new Promise(resolve =>{
					if(targetElement == this && node.nodeType == Node.TEXT_NODE){
						resolve(undefined);
					}
					resolve( this.#toJSON(node, {afterCallback}) )
				})
			})).then(jsonList=>jsonList.filter(e=>e != undefined).map(e=> {
				delete e.node;
				return e;
			}))
	}

	async #toJSON(node, {afterCallback}){
		return new Promise(resolve=>{
			let obj = {};
			if(node.nodeType == Node.TEXT_NODE && node.textContent != '' && node.textContent){
				obj.type = Node.TEXT_NODE;
				obj.name = node.constructor.name;
				obj.text = node.textContent
				obj.node = node;
				afterCallback(obj);
				resolve(obj);
			}else if(node.nodeType == Node.ELEMENT_NODE){
				obj.type = Node.ELEMENT_NODE;
				obj.name = node.constructor.name;
				obj.tagName = node.localName;
				obj.data = Object.assign({}, node.dataset);
				obj.node = node;
				if(node.hasAttribute('is_cursor')){
					obj.cursor_offset = node.getAttribute('cursor_offset');
					obj.cursor_type = node.getAttribute('cursor_type');
					obj.cursor_index = node.getAttribute('cursor_index');
					obj.cursor_scroll_x = node.getAttribute('cursor__scroll_x');
					obj.cursor_scroll_y = node.getAttribute('cursor_scroll_y');
				}
				this.getLowDoseJSON(node, {afterCallback})
				.then(jsonList => {
					obj.childs = jsonList.filter(e=> e != undefined)
					afterCallback(obj);
					resolve(obj);
				})
			}else{
				//afterCallback(undefined)
				resolve(undefined);
			}
			
		})
	}

	async parseLowDoseJSON(json, {beforeCallback = (json) => {}, afterCallback = (node)=> {}} = {}){
		return new Promise(resolve => {
			let jsonObj = json;
			if(typeof json == 'string'){
				jsonObj = JSON.parse(json);
			}

			if(jsonObj instanceof Array){
				resolve(
					Promise.all(this.#toHTML(jsonObj, {beforeCallback, afterCallback}))
					.then(htmlList => {
						this.replaceChildren(...htmlList.filter(e=> e != undefined))
					})
				);
			}
			resolve(undefined);
		})
	}

	#toHTML(objList, {beforeCallback, afterCallback}){
		return objList.filter(e=>e!=undefined).map(async jsonNode => {
			return new Promise(resolve => {
				beforeCallback(jsonNode);
				let node = undefined;
				if(jsonNode.type == Node.TEXT_NODE){
					node = document.createTextNode(jsonNode.text);
				}else if(jsonNode.type == Node.ELEMENT_NODE){
					let EditorTarget = FreeWillEditor.componentsMap[jsonNode.name] || FreeWillEditor.toolsMap[jsonNode.name]
					if(EditorTarget){
						node = new EditorTarget(jsonNode.data);
					}else if(jsonNode.data.hasOwnProperty('is_line')){
						let line = new Line(document.createElement(jsonNode.tagName));
						node = line.lineElement;
						Object.assign(node.dataset, jsonNode.data);
					}else{
						//node = document.createElement(jsonNode.name.replaceAll(/HTML|Element/g, '').toLowerCase());
						node = document.createElement(jsonNode.tagName);
						Object.assign(node.dataset, jsonNode.data);
					}
					afterCallback(node)
					if(jsonNode.childs.length != 0){
						Promise.all(this.#toHTML(jsonNode.childs, {beforeCallback, afterCallback})).then(childList => {
							node.append(...childList);
						})
					}
				}
				resolve(node);
			})
		})
	}

	set placeholder(placeholder){
		this.#placeholder = placeholder;
		if(this.firstElementChild && this.contentEditable != false){
			this.firstElementChild.setAttribute('data-placeholder', this.#placeholder);
		}
	}

	get firstLine(){
		return this.firstElementChild;
	}

	get isEmpty(){
		return Line.isElementTextEmpty(this) && ! [...this.querySelectorAll('*')].some(e=>this.tools.hasOwnProperty(e.localName));
	}

	get isLoaded(){
		return this.#isLoaded;
	}
}

;// CONCATENATED MODULE: ./view/js/handler/workspace/WorkspaceHandler.js

/* harmony default export */ const WorkspaceHandler = (new class WorkspaceHandler{
    #workspace;
    #workspaceId;
    #addWorkspaceIdChangedListener = {};
    #workspaceChangeDone;
    #workspaceChangeAwait;
    constructor(){
        //window.addEventListener("DOMContentLoaded", (event) => {
            window.myAPI.getWorkspaceId().then(workspaceId=>{
                this.workspaceId = workspaceId;
            })
            window.myAPI.event.electronEventTrigger.addElectronEventListener('workspaceChange', event => {
                this.workspaceId = event.workspaceId;
            })
        //});
    }

    set addWorkspaceIdChangedListener({name, callBack, runTheFirst}){
        this.#addWorkspaceIdChangedListener[name] = callBack;
        if(runTheFirst && this.workspaceId){
            callBack(this);
        }
    }
    get addWorkspaceIdChangedListener(){
        return this.#addWorkspaceIdChangedListener;
    }

    set workspaceId(workspaceId){
        if(!workspaceId)return;
        this.#workspaceId = workspaceId;
        window.myAPI.workspace.getWorkspaceDetail({workspaceId}).then((workspace) => {
            this.#workspace = workspace;
            let startCallbackPromise = Promise.all(Object.values(this.#addWorkspaceIdChangedListener).map(async callBack => {
                return new Promise(res => {
                    callBack(this);
                    res();
                });
            }));
            if( ! this.#workspaceChangeAwait){
                this.#workspaceChangeAwait = new Promise(resolve=>{
                    this.#workspaceChangeDone = resolve;
                    this.#workspaceChangeDone(startCallbackPromise);
                })
            }else{
                this.#workspaceChangeAwait.then(() => {
                    startCallbackPromise.then(() => {
                        this.#workspaceChangeAwait = new Promise(resolve => {
                            this.#workspaceChangeDone = resolve;
                        })
                    })
                })
            }
        });
    }
    get workspaceId(){
        return Number(this.#workspaceId);
    }
    get workspace(){
        return this.#workspace;
    }
    removeWorkspaceIdChangedListener(name){
        delete this.#addWorkspaceIdChangedListener(name);
    }
});
;// CONCATENATED MODULE: ./view/js/handler/room/RoomHandler.js
/* harmony default export */ const RoomHandler = (new class RoomHandler{
    #roomId
    #room;
    #addRoomIdChangeListener = {};

    #roomChangeDone;
    #roomChangeAwait = new Promise(resolve=>{
        this.#roomChangeDone = resolve;
    });

    constructor(){

        window.myAPI.event.electronEventTrigger.addElectronEventListener('roomChange', event => {
            this.roomId = event.roomId
        })

    }

    set addRoomIdChangeListener({name, callBack, runTheFirst}){
        this.#addRoomIdChangeListener[name] = callBack;
        if(runTheFirst && this.#roomId){
            callBack(this);
        }
    }

    get addRoomIdChangeListener(){
        return this.#addRoomIdChangeListener;
    }

    set roomId(roomId){
        if( ! roomId){
            throw new Error('roomId is undefined')
        }
        this.#roomId = roomId;
        window.myAPI.room.getRoomDetail({roomId}).then(result => {
            this.#room = result.data;
            window.myAPI.setOption({
                name: 'lastRoomInfo', value : JSON.stringify(this.#room)
            })
            let startCallbackPromise = Promise.all(
                Object.values(this.#addRoomIdChangeListener).map(async callBack => {
                    return new Promise(res => {
                        callBack(this);
                        res();
                    })
                })
            )
            startCallbackPromise.then(()=>{
                this.#roomChangeDone();
            })
            this.#roomChangeAwait.then(()=>{
                this.#roomChangeAwait = new Promise(resolve=>{
                    this.#roomChangeDone = resolve;
                })
            })
            //window.myAPI.setTitle({title:this.#room.roomName})
            
        });
    }

    get roomId(){
        return Number(this.#roomId);
    }

    get room(){
        return this.#room;
    }
    removeRoomIdChangeListener(name){
        delete this.#addRoomIdChangeListener(name);
    }
    get roomChangeAwait(){
        return this.#roomChangeAwait;
    }
});
;// CONCATENATED MODULE: ./view/js/common.js
/* harmony default export */ const common = (new class Common{
	#keyRegx = /[A-Z]?[a-z]+|[0-9]+|[A-Z]+(?![a-z])/g;

	constructor(){

	}

	jsonToSaveElementDataset(data, element){
		if( ! element){
			throw new Error('element is undefined')
		} else if(element.nodeType != Node.ELEMENT_NODE){
			throw new Error(`element is not element node type ::: ${element.nodeType}`);
		}
		return new Promise(resolve=>{
			let underbarKeyNameObject = Object.entries(data).reduce((total, [k,v]) => {
				let key = k.match(this.#keyRegx).map(e=> e.toLowerCase()).join('_');
				total[key] = v;
				return total;
			}, {});
			Object.assign(element.dataset, underbarKeyNameObject);
			resolve(element);
		})
	}
	async underbarNameToCamelName(obj){
		return new Promise(resolve => {
			resolve(Object.entries(obj).reduce((total, [k,v]) => {
				let key = k.split('_').map((e,i)=>{
					if(i == 0){
						return e.charAt(0).toLowerCase() + e.substring(1);
					}
					return e.charAt(0).toUpperCase() + e.substring(1)
				}).join('');
				total[key] = v;
				return total;
			}, {}))
		})
	}
	/**
	 * @returns {Promise<String>}
	 */
	async getProjectPathPromise(){
		return await window.myAPI.getProjectPath().then((data) => {
			return data;
		}).catch(error=>{
			console.error(error);
			return '';
		})
	}

	processingElementPosition(element, target){
		let rect;
		if(this.isElement(target, HTMLElement)){
			rect = target.getBoundingClientRect();
		}else{
			rect = target;
		}

		let {x, y, height, width} = rect;
		let elementTop = (y - element.clientHeight)
		let elementLeft = (x - element.clientWidth)
		if(elementTop > 0){
			element.style.top = elementTop + 'px';
		}else{
			element.style.top = y + height + 'px';
		}
		if(elementLeft > 0){
			element.style.left = elementLeft + 'px'
		}else{
			element.style.left = x + width + 'px';
		}
		
	}
    isElement(targetObject, checkClazz){
        let check = Object.getPrototypeOf(targetObject)
        let isElement = false;
        while(check != undefined){

            if(check?.constructor  == checkClazz){
                isElement = true;
                break;
            }else{
                check = Object.getPrototypeOf(check);
            }
        }
        return isElement;
    }

	shortenBytes(byte) {
		const rank = byte > 0 ? Math.floor((Math.log2(byte)/10)) : 0;
		const rankText = ( (rank > 0 ? 'KMGTPEZY'[rank - 1] : '') || (rank >= 9 ? 'Y' : '') ) + 'B';
		const size = Math.floor(byte / Math.pow(1024, (rank >= 9 ? 8 : rank) ));
		return {size, rank, rankText};
	}
});
;// CONCATENATED MODULE: ./view/js/handler/PositionChangeer.js
class PositionChanger{
	#wrapper;
	#targetItem;
	#childList = [];
	#onDropEndChangePositionCallback = (changeList) => {};
	#onDropDocumentOutCallback = (target) => {console.log('out!!', target)}; 
	#onIfCancelCallback = (target) => {return false;};
	constructor({wrapper}){
		if( ! wrapper){
			throw new Error('wrapper is undefined');
		}/*else if(childList.length == 0){
			throw new Error('child list is empty');
		}*/
		this.#wrapper = wrapper;
	}

	addPositionChangeEvent(child, wrapper){
		if(child.length == 0){
			throw new Error('child is empty');
		}else if(child.some(e=>Number(e.dataset.order_sort) == undefined || isNaN(Number(e.dataset.order_sort)))){
			console.error(child);
			throw new Error('data-order_sort is not defined or is not number')
		}
		let parent = wrapper || this.#wrapper;
		parent.dataset.is_position_change = '';
		let pointerEventTargetList = [];
		return new Promise(resolve => {
			
			let lastItem = [...parent.children].filter(e=>e.hasAttribute('data-order_sort')).pop() // or at(-1)
			child.forEach((item, index)=>{
				item.draggable = true;
				item.ondragstart = (event) => {
					event.stopPropagation();
					//event.preventDefault();
					this.#targetItem = item;
					//this.#targetItem = event.target;
					child.forEach(async e => {
						[...e.children].forEach((ee)=>{
							if(ee.hasAttribute('data-is_position_change')) {
								return;
							}
							ee.style.pointerEvents = 'none';
							ee.dataset.is_pointer_target = ''
						})
					})
				}
				item.ondragend = (event) => {
					event.stopPropagation();
					//event.preventDefault();
					if(
						event.x < 0 || event.y < 0 ||
						window.outerWidth < event.x || window.outerHeight < event.y
					){
						this.#onDropDocumentOutCallback({
							target:item,
							event
						});
					}
					this.#targetItem = undefined;
					
					new Promise(res => {
						document.querySelectorAll('[data-is_pointer_target]').forEach(e=>{
							e.style.pointerEvents = '';
							e.removeAttribute('data-is_pointer_target');
						})
						res();
					})
					if(item.__dragendCallback){
						item.__dragendCallback();
					}
				}
				item.ondragover = (event) => {
					event.stopPropagation();
					event.preventDefault();
				}
				item.ondragenter = (event) => {
					event.stopPropagation();
					event.preventDefault();
					event.target.style.borderTop = 'solid #0000009c';
				}
				item.ondragleave = (event) => {
					event.stopPropagation();
					event.preventDefault();
					event.target.style.borderTop = '';
				}
				item.ondrop = (event) => {
					event.stopPropagation();
					event.preventDefault();
					event.target.style.borderTop = '';

					if(! this.#targetItem){
						return;
					}
					//return;
					let target = this.#targetItem;
					let cancle = this.onIfCancelCallback(target, item)
					if((typeof cancle) != 'boolean'){
						throw new Error('onIfCancelCallback is only return Boolean');
					}else if(cancle){
						return;
					}
					//item.before(target);
					event.target.closest('[data-order_sort]').before(target);
					this.#targetItem = undefined;
					new Promise(res=>{
						let nowLastItem = [...parent.children].filter(e=>e.hasAttribute('data-order_sort')).pop(); // or at(-1)
						//let nowLastItem = [...target.closest('ul').children].filter(e=>e.hasAttribute('data-order_sort')).pop(); // or at(-1)
						//this.#wrapper.querySelector('[data-order_sort]:last-child');
						if( ! lastItem){
							lastItem = nowLastItem;
						}
						let prevOrderSort = Number(lastItem.dataset.order_sort);
						if(target == lastItem && lastItem != nowLastItem){
							nowLastItem.dataset.order_sort = lastItem.dataset.order_sort;
							prevOrderSort = Number(lastItem.dataset.order_sort);
							//prevOrderSort -= 1;
						}
						lastItem = nowLastItem
						let prevItem = lastItem?.previousElementSibling;
						while(prevItem){
							prevOrderSort += 1;
							prevItem.dataset.prev_order_sort = prevItem.dataset.order_sort
							prevItem.dataset.order_sort = prevOrderSort ;
							if(prevItem.previousElementSibling === prevItem){
								break;
							}
							prevItem = prevItem.previousElementSibling;
						}
						res();
					}).then(()=>{
						this.#onDropEndChangePositionCallback(
							[...new Set([...child.filter(e=>e.dataset.order_sort != e.dataset.prev_order_sort && e != target), target])],
							{item, target, parent}
						)
					})
				}
			})
			resolve();
		});
	}

	set onDropEndChangePositionCallback(callBack){
		this.#onDropEndChangePositionCallback = callBack;
	}
	get onDropEndChangePositionCallback(){
		return this.#onDropEndChangePositionCallback;
	}

	set onDropDocumentOutCallback(callBack){
		this.#onDropDocumentOutCallback = callBack;
	}
	get onDropDocumentOutCallback(){
		return this.#onDropDocumentOutCallback;
	}

	set onIfCancelCallback(callBack){
		this.#onIfCancelCallback = callBack
	}
	get onIfCancelCallback(){
		return this.#onIfCancelCallback;
	}

}
;// CONCATENATED MODULE: ./view/js/handler/account/AccountHandler.js
const accountHandler = new class AccountHandler{
    #accountInfo = this.searchAccountInfo();
    constructor(){

    }

    async searchAccountInfo(){
        this.#accountInfo = await window.myAPI.account.getAccountInfo().then(result => {
            return result.data;
        });
        return this.#accountInfo;
    }

    get accountInfo(){
        return this.#accountInfo;
    }
};
;// CONCATENATED MODULE: ./view/js/handler/S3EncryptionUtil.js

const s3EncryptionUtil = new class S3EncryptionUtil{

	encoder = new TextEncoder();
	decoder = new TextDecoder();

	signAlgorithm = {
		name: "RSASSA-PKCS1-v1_5",
		// Consider using a 4096-bit key for systems that require long-term security
		modulusLength: 2048,
		publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
		hash: "SHA-256",
	}

	secretAlgorithm = {
		name: "RSA-OAEP",
		modulusLength: 2048,
		publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
		hash: "SHA-256",
	}

	async generateKeyPair(algorithm, keyUsages){
		return window.crypto.subtle.generateKey(
			algorithm,
			true,
			keyUsages//["sign", "verify"]
		);
	}

	async keySign(data, privateKey){
		let message = this.encoder.encode(data);
		return window.crypto.subtle.sign(this.signAlgorithm.name, privateKey, message).then(signature=>{
			return {message, signature};
		})
	}

	async decryptMessage(privateKey, ciphertext, algorithm) {
		return window.crypto.subtle.decrypt(
			{ name: algorithm.name },
			privateKey,
			ciphertext,
		).catch(err=>{
			console.error(err.message);
			console.error(err.stack);
		});
	}

	
	async convertBase64ToBuffer(base64){
		return fetch(`data:application/octet-binary;base64,${base64}`)
		.then(res=>res.arrayBuffer())
		.then(buffer=>new Uint8Array(buffer))
	}

	async exportKey(exportType, key){
		return window.crypto.subtle.exportKey(exportType, key).then(exportKey => {
			return new Promise( resolve => resolve(String.fromCharCode(...new Uint8Array(exportKey))) );
		}).then(exportKeyString => {
			return new Promise( resolve => resolve(window.btoa(exportKeyString)) );
		});
	}
	async callS3PresignedUrl(callFunction, signData, callFunctionParam = {} ){//,fileName, accountName){
		console.log('callFunctionParam !!! ',callFunctionParam);
		return Promise.all( [this.generateKeyPair(this.signAlgorithm, ["sign", "verify"]), this.generateKeyPair(this.secretAlgorithm, ["encrypt", "decrypt"])] )
		.then( ([signKeyPair, encDncKeyPair]) => {
			return Promise.all( [
				this.exportKey('spki', signKeyPair.publicKey),
				this.exportKey('spki', encDncKeyPair.publicKey), 
				Promise.resolve(encDncKeyPair), 
				Promise.resolve(signKeyPair)
			] )		
		}).then( async ([exportSignKey, exportEncKey, encDncKeyPair, signKeyPair]) => {

			let sign = await this.keySign(
				`${signData}:${exportEncKey}`, 
				signKeyPair.privateKey
			)
			
			let result = await callFunction(Object.assign(callFunctionParam, {
				data: window.btoa(String.fromCodePoint(...sign.message)), 
				dataKey: exportSignKey, 
				sign: window.btoa( String.fromCodePoint(...new Uint8Array(sign.signature)) ), 
			}))

			let {code, data} = result;
			
			if(code != 0){
				return ;
			}
			
			return {data, encDncKeyPair};
		})
	}

	async fetchPutObject(putUrl, key, md5, fileData){
		return fetch(putUrl, {
			method:"PUT",
			headers: {
				'Content-Encoding' : 'base64',
				'Content-Type' : 'application/octet-stream',
				'x-amz-server-side-encryption-customer-algorithm': 'AES256',
				'x-amz-server-side-encryption-customer-key': key,
				'x-amz-server-side-encryption-customer-key-md5': md5,
			},
			body: fileData
		})
	}
}
;// CONCATENATED MODULE: ./view/js/component/notice_board/notice_board_item/NoticeBoardDetail.js




























class NoticeBoardLine extends FreeWillEditor{
    static{
        window.customElements.define('notice-board-line', NoticeBoardLine);
    }
	static tools = [
        Strong,
        Color,
        Background,
        Strikethrough,
        Underline,
        FontFamily,
        Quote,
        NumericPoint,
        BulletPoint,
        Sort,
        FontSize,
        Italic,
        Image,
        Video,
        Resources,
        Code,
        Hyperlink,
    ]

    static option = {
        isDefaultStyle : true
    }
    
    parentLi;
    parentClass;
    constructor(parentLi, parentClass){

		super(NoticeBoardLine.tools, NoticeBoardLine.option);

        this.parentLi = parentLi;
        this.parentClass = parentClass;
        
        super.placeholder = ''
        super.spellcheck = true
    }

}

/* harmony default export */ const NoticeBoardDetail = (new class NoticeBoardDetail{
	#memory = {}

    #element = Object.assign(document.createElement('div'), {
        id: 'notice_board_detail_wrapper',
        innerHTML: `
            <div class="notice_board_detail_container list_scroll list_scroll-y" data-bind_name="noticeBoardDetailContainer">
                <ul class="notice_board_detail_content" data-bind_name="noticeBoardDetailList">
 
                </ul>
                <div class="toolbar" style="position: fixed; z-index: 2000;" data-bind_name="toolbar"></div>
            </div>
        `
    })

	#elementMap = (()=>{
		return 	[...this.#element.querySelectorAll('[data-bind_name]')].reduce((total, element) => {
			total[element.dataset.bind_name] = element;
			return total;
		}, {})
	})();

    #positionChanger;

    /*#prevRange;
    #prevContent;
    #prevStartOffset;
    #prevEndOffset;*/

    constructor(){
        this.#positionChanger = new PositionChanger({wrapper: this.#elementMap.noticeBoardDetailList});
		this.#positionChanger.onDropEndChangePositionCallback = (changeList, {item, target, wrapper}) => {
            window.myAPI.noticeBoard.updateNoticeBoardDetailOrder(
                changeList.map(e=>{
                    return {
                        id: e.dataset.id,
                        workspaceId: e.dataset.workspace_id,
                        roomId: e.dataset.room_id,
                        orderSort: e.dataset.order_sort,
                    }
                })
            ).then(result => {
                console.log(result);
            })
        };

        window.myAPI.event.electronEventTrigger.addElectronEventListener('noticeBoardDetailAccept', (data) => {
            //console.log(data);
            let isEventStream
            if(data && data.serverSentStreamType){
                isEventStream = true;
                data = data.content;
            }
            
            let lastTarget = document.activeElement;
            this.createItemElement(data)
            .then(li => {
                this.#addMemory(li, data.workspaceId, data.roomId, data.noticeBoardId, data.id);
                let list = Object.values(this.#memory[data.workspaceId]?.[data.roomId]?.[data.noticeBoardId] || {})
                .sort((a,b) => Number(b.dataset.order_sort) - Number(a.dataset.order_sort));
                
                this.#positionChanger.addPositionChangeEvent(list);

                let listObserver = new MutationObserver( (mutationList, observer) => {
                    mutationList.forEach((mutation) => {
                        let {addedNodes, removedNodes} = mutation;
                        let isAddedActiveTarget = [...addedNodes].some(e=> e == lastTarget.parentLi)
                        if(isAddedActiveTarget){
                            //setTimeout(()=>{
                                lastTarget.contentEditable = true;
                                let cursorTarget = lastTarget.hasAttribute('is_cursor') ? lastTarget : lastTarget.querySelector('[is_cursor]');
                                if( ! cursorTarget) return;
                                let {'cursor_offset': offset, 'cursor_type': type, 'cursor_index': index, 'cursor_scroll_x': x, 'cursor_scroll_y': y} = cursorTarget.attributes;
                                let selection = window.getSelection();
                                if(type.value == Node.ELEMENT_NODE){
                                    let node = cursorTarget.childNodes[index.value]; 
                                    selection.setPosition(node, offset.value);
                                }else if(type.value == Node.TEXT_NODE){
                                    selection.setPosition(cursorTarget, offset.value);
                                }

                            //},5000)
                        }
                    })
                });
                if(lastTarget && lastTarget.tagName == 'NOTICE-BOARD-LINE'){ //|| ! checkTarget){    
                    listObserver.observe(this.#elementMap.noticeBoardDetailList, {childList:true});
                }

                this.#elementMap.noticeBoardDetailList.replaceChildren(...list);

                //this.#elementMap.noticeBoardDetailList.append();
                let appendAwait = setInterval(()=>{
                    if( ! li.isConnected){
                        return;
                    }
                    clearInterval(appendAwait);
                    listObserver.disconnect();
                }, 50)
            })
        })

        this.#elementMap.noticeBoardDetailList
        NoticeBoardHandler.addNoticeBoardIdChangeListener = {
            name: 'noticeBoardDetailIdChange',
            callBack: (handler, data)=>{
                this.#elementMap.noticeBoardDetailList.replaceChildren();
                this.refresh();
                /*if(this.#memory[workspaceHandler.workspaceId]?.[roomHandler.roomId]?.[noticeBoardHandler.noticeBoardId]){
                    this.#memory[workspaceHandler.workspaceId][roomHandler.roomId][noticeBoardHandler.noticeBoardId] = {};
                }*/
            }
        }

        let toolList = Object.values(NoticeBoardLine.tools).map(e=>e.toolHandler.toolButton);

        document.addEventListener('selectionchange', event => {
            if(document.activeElement.constructor != NoticeBoardLine){
                return;
            }
            let selection = window.getSelection();
            if (selection.rangeCount == 0){
                return;
            }
            
            /*let range = selection.getRangeAt(0);

            this.#prevRange = range.cloneRange();
            this.#prevStartOffset = range.startOffset;
            this.#prevEndOffset = range.endOffset;
            this.#prevContent = range.commonAncestorContainer;*/

            if( ! selection.isCollapsed){
                this.#elementMap.noticeBoardDetailContainer.append(this.#elementMap.toolbar);
                this.#elementMap.toolbar.replaceChildren(...toolList);
                common.processingElementPosition(this.#elementMap.toolbar, window.getSelection().getRangeAt(0).getBoundingClientRect())
            }else{
                this.#elementMap.toolbar.remove();
            }
        })
        this.#elementMap.noticeBoardDetailContainer.addEventListener("scroll", () => {
			if(this.#elementMap.toolbar.childElementCount == 0)return;
			common.processingElementPosition(this.#elementMap.toolbar, window.getSelection().getRangeAt(0).getBoundingClientRect());
		});
        window.addEventListener('resize', (event) => {
			if(this.#elementMap.toolbar.childElementCount == 0)return;
            common.processingElementPosition(this.#elementMap.toolbar, window.getSelection().getRangeAt(0).getBoundingClientRect());
		})
    }

    createItemElement(data){
        return new Promise(resolve=>{
            let li = Object.assign(document.createElement('li'),{
                className : 'notice_board_detail_item'
            });
            li.dataset.is_not_visible_target = '';
            li.style.minHeight = (parseInt(window.getComputedStyle(document.body).fontSize) || 16) * 2 + 'px';
            
            resolve(this.#addItemEvent(li, data));
        })
    }

    #addItemEvent(li, data){
        return new Promise(resolve=> {
            let addButton = Object.assign(document.createElement('button'), {
                className: 'notice_board_detail_item_add_content',
                textContent: '+'
            });
            let editor = new NoticeBoardLine(li, this);
            let positionChangeIcon = Object.assign(document.createElement('span'),{
                className: 'notice_board_detail_item_position_change_icon pointer',
                textContent: '〓'
            })

            let datasetPromise = common.jsonToSaveElementDataset(data, li);
            //console.log(data.content);
            if(data.content){
                let {content} = data;
                delete data.content;
                datasetPromise.then(() => {
                    li.removeAttribute('data-content');
                    
                    editor.parseLowDoseJSON(content).then(() => {
                        editor.contentEditable = false;
                        li.append(editor);
                        let appendAwait = setInterval(()=>{
                            if( ! editor.isConnected) return;
                            clearInterval(appendAwait);
                            if(editor.isEmpty){
                                editor.remove();
                                li.prepend(addButton);
                            }
                            /*if( ! editor.isEmpty){
                                li.append(positionChangeIcon)
                            }else {
                                editor.remove();
                                li.prepend(addButton);
                            }*/
                        },50)
                    })
                })
            }else{
                li.prepend(addButton);
                editor.startFirstLine();
            }
            li.onmouseenter = () => {
                if(addButton.isConnected){
                    addButton.classList.add('active');
                    return;
                }
                li.draggable = false;
                if( ! editor.isEmpty){
                    li.append(positionChangeIcon)
                }
            }
            li.onmouseleave = () => {
                if(addButton.isConnected){
                    addButton.classList.remove('active');
                    return;
                }
                if(positionChangeIcon.isConnected){
                    positionChangeIcon.remove();
                }
            }
            //let isPositionChangeIconOver = false;
            positionChangeIcon.onmousemove = () => {
                li.draggable = true;
            }
            positionChangeIcon.onmouseover = () => {
                //isPositionChangeIconOver = true;
                li.draggable = true;
            }
            positionChangeIcon.onmouseout = () => {
                //isPositionChangeIconOver = false;
                li.draggable = false;
            }

            addButton.onclick = (event) => {
                //console.log(event, editor.isConnected);
                editor.contentEditable = true;
                editor.firstLine.innerText = '\n'
                if(editor.isConnected){
                    return;
                }
                li.append(editor);
                li.append(positionChangeIcon);
                addButton.remove();
                window.getSelection().setPosition(editor, editor.childNodes.length);
            }
            let prevText;
            editor.onclick = () => {
                if(editor.contentEditable == 'false'){
                    editor.contentEditable = true;
                    if(editor.isEmpty){
                        editor.firstLine.innerText = '\n'
                    }
                    window.getSelection().setPosition(editor, editor.childNodes.length);
                }
            }
            editor.onfocus = (event) => {
                prevText = editor.innerHTML;
            }
            let isScriptBlur = false;
            editor.onblur = (event) => {
                if( ! isScriptBlur && (editor.matches(':hover') || this.#elementMap.toolbar.matches(':hover') || document.activeElement == editor)){
                    return;
                }
                if(isScriptBlur){
                    isScriptBlur = ! isScriptBlur;
                }
                this.#elementMap.toolbar.remove();
                editor.contentEditable = false;
                if( ! event.relatedTarget?.hasAttribute('data-tool_status')){
                    this.#elementMap.toolbar.remove();
                }
                if(editor.isEmpty){
                    editor.replaceChildren();
                    editor.remove();
                    positionChangeIcon.remove();
                    li.prepend(addButton);
                    if(li.matches(':hover')){
                        addButton.classList.add('active');
                    }else{
                        addButton.classList.remove('active');
                    }
                    //return ;
                }else if(prevText == editor.innerHTML){
                    return;
                }
                prevText = editor.innerHTML;
                
                let param = {
                    id: li.dataset.id,
                    noticeBoardId: NoticeBoardHandler.noticeBoardId,
                    roomId: RoomHandler.roomId,
                    workspaceId: WorkspaceHandler.workspaceId,
                    orderSort: li.dataset.order_sort //([...this.#elementMap.noticeBoardDetailList.children].findIndex(e=>e==li) - 1) * -1,
                };
                this.#uploadNoticeBoard(editor, param);
            }
            editor.onkeydown = (event) => {
                let {altKey, ctrlKey, shiftKey, key} = event;
                if(key == 'Enter' && (altKey || ctrlKey || shiftKey)){
                    event.preventDefault();
                    let LineBreakMode;
                    if(altKey){
                        LineBreakMode = FreeWillEditor.LineBreakMode.NEXT_LINE_FIRST
                    }else if(ctrlKey){
                        LineBreakMode = FreeWillEditor.LineBreakMode.NEXT_LINE_LAST
                    }else{
                        LineBreakMode = FreeWillEditor.LineBreakMode.NO_CHANGE
                    }
                    editor.lineBreak(LineBreakMode);
                }else if(key == 'Enter'){// && editor.innerText.replaceAll('\n', '') != ''){
                    event.preventDefault();
                    isScriptBlur = true;
                    editor.blur();
                }
            }

            resolve(li);
        });
    }

    /**
     * 
     * @param {NoticeBoardLine} editor 
     * @param {Object} param
     */
    async #uploadNoticeBoard(editor, param){
        let promiseList = [];

        editor.contentEditable = false;
        editor.getLowDoseJSON(editor, {
            afterCallback : (json) => {
                if(json.tagName != Image.toolHandler.defaultClass && 
                    json.tagName != Video.toolHandler.defaultClass &&
                    json.tagName != Resources.toolHandler.defaultClass
                ){
                    return;
                }else if(json.data.hasOwnProperty('is_upload_end')){
                    return;
                }
                let node = json.node;
                let file = node.selectedFile;
                json.data.is_loading = '';
                console.log(node, file);
                let fileType;
                if(json.tagName == Image.toolHandler.defaultClass){
                    fileType = 'IMAGE';
                }else if(json.tagName == Video.toolHandler.defaultClass){
                    fileType = 'VIDEO';
                }else {
                    fileType = 'FILE';
                }
                promiseList.push(new Promise(async resolve => {

                    let {name, size, lastModified, contentType, newFileName} = await common.underbarNameToCamelName(json.data);
                    let putSignData = `${RoomHandler.roomId}:${WorkspaceHandler.workspaceId}:${name}:${accountHandler.accountInfo.accountName}`;
                    let isUpload = await s3EncryptionUtil.callS3PresignedUrl(window.myAPI.s3.generatePutObjectPresignedUrl, putSignData, {newFileName, fileType, uploadType: 'NOTICE'})
                    .then( (result) => {
                        if(! result){
                            return;
                        }
                        let {data, encDncKeyPair} = result;

                        json.data.new_file_name = data.newFileName;

                        return Promise.all([
                            s3EncryptionUtil.convertBase64ToBuffer(data.encryptionKey).then( async (buffer) => {
                                return s3EncryptionUtil.decryptMessage(encDncKeyPair.privateKey, buffer, s3EncryptionUtil.secretAlgorithm)
                                    .then(buf=>String.fromCharCode(...new Uint8Array(buf)))
                            }),
                            s3EncryptionUtil.convertBase64ToBuffer(data.encryptionMd).then( async (buffer) => {
                                return s3EncryptionUtil.decryptMessage(encDncKeyPair.privateKey, buffer, s3EncryptionUtil.secretAlgorithm)
                                    .then(buf=>String.fromCharCode(...new Uint8Array(buf)))
                            })
                        ]).then( async ([k,m]) => {
                            /*let base64 = json.data.base64;
                            if(! base64){
                                let blob = await fetch(json.data.url).then(res=>res.blob())
                                base64 = await new Promise(resolve => {
                                    const reader = new FileReader();
                                    reader.readAsDataURL(blob);
                                    reader.onloadend = () => {
                                        resolve(reader.result);
                                    }
                                });
                            }*/
                            let res = await s3EncryptionUtil.fetchPutObject(data.presignedUrl, k, m, file.files[0]);
                            if( ! (res.status == 200 || res.status == 201) ){
                                return;
                            }
                            return true;
                        })
                    })
                    console.log(isUpload);
                    if( ! isUpload){
                        resolve();
                        return;
                    }
                    let getSignData = `${RoomHandler.roomId}:${WorkspaceHandler.workspaceId}:${json.data.new_file_name}:${accountHandler.accountInfo.accountName}`;
                    
                    s3EncryptionUtil.callS3PresignedUrl(window.myAPI.s3.generatePutObjectPresignedUrl, getSignData, {fileType, uploadType: 'NOTICE'})
                    .then( (result) => {
                        if(! result){
                            return;
                        }
                        let {data, encDncKeyPair} = result;

                        json.data.url = data.presignedUrl;
                        delete json.data.base64
                        json.data.upload_type = 'NOTICE';
                        resolve(json);
                    })
                }))
            }
        }).then(jsonList => {

            param.content = JSON.stringify(jsonList);
            if(param.content.length == 0) param.content = undefined
            window.myAPI.noticeBoard.createNoticeBoardDetail(param).then(res=>{
                let {data} = res
                this.innerText = '';
                Promise.all(promiseList).then((fileTargetList) => {
                    console.log(fileTargetList);
                    if(fileTargetList.length == 0){
                        //window.getSelection().setPosition(this, 1)
                        return;
                    }
                    fileTargetList.forEach(e=>{
                        delete e.data.is_loading
                        e.data.is_upload_end = '';
                    });
                    param.content = JSON.stringify(jsonList);
                    if(param.content.length == 0) param.content = undefined
                    window.myAPI.noticeBoard.createNoticeBoardDetail(param).then(response=>{
                        //window.getSelection().setPosition(this, 1)
                    });
                })
            });
            
        })
    }

    #callData(){
        return window.myAPI.noticeBoard.searchNoticeBoardDetailList({
            workspaceId: WorkspaceHandler.workspaceId, 
            roomId: RoomHandler.roomId,
            noticeBoardId : NoticeBoardHandler.noticeBoardId
        })
    }

    #addMemory(data, workspaceId, roomId, noticeBoardId, id){
		if( ! this.#memory.hasOwnProperty(workspaceId)){
			this.#memory[workspaceId] = {};
		}
		if( ! this.#memory[workspaceId].hasOwnProperty(roomId)){
			this.#memory[workspaceId][roomId] = {} ;
		}
        if( ! this.#memory[workspaceId][roomId].hasOwnProperty(noticeBoardId)){
            this.#memory[workspaceId][roomId][noticeBoardId] = {}
        }
        
        this.#memory[workspaceId][roomId][noticeBoardId][id] = data;
		
    }
    async refresh(){
        let list = Object.values(this.#memory[WorkspaceHandler.workspaceId]?.[RoomHandler.roomId]?.[NoticeBoardHandler.noticeBoardId] || {})
        .sort((a,b) => Number(b.dataset.order_sort) - Number(a.dataset.order_sort));
        if(list.length == 0){
            this.#callData();
            return;
        }
        this.#positionChanger.addPositionChangeEvent(list);
        this.#elementMap.noticeBoardDetailList.replaceChildren(...list);  

	}

	get element(){
		return this.#element;
	}
	
	get elementMap(){
		return this.#elementMap;
	}
});
;// CONCATENATED MODULE: ./view/js/handler/chatting/ChattingHandler.js

/* harmony default export */ const ChattingHandler = (new class ChattingHandler{
	#lastChattingId;
	#lastChatting;
    #addChattingEventListener = {};
    constructor(){
		window.myAPI.event.electronEventTrigger.addElectronEventListener('chattingAccept', data => {
			let {content} = data;
			//let {id, accountId, accountName, chatting, createAt, createBy, roomId, updateAt, updateBy} = data.content;
			this.#lastChattingId = content.id;
			this.#lastChatting = content;
			/*
			data:"{\"id\":null,\"accountId\":null,\"accountName\":\"test\",\"roomId\":null,\"chatting\":\"[{\\\"type\\\":1,\\\"name\\\":\\\"Line\\\",\\\"data\\\":{},\\\"cursor_offset\\\":\\\"10\\\",\\\"cursor_type\\\":\\\"3\\\",\\\"cursor_index\\\":\\\"0\\\",\\\"cursor_scroll_x\\\":null,\\\"cursor_scroll_y\\\":\\\"0\\\",\\\"childs\\\":[{\\\"type\\\":3,\\\"name\\\":\\\"Text\\\",\\\"text\\\":\\\"qweasdxzxc\\\"}]}]\",\"createAt\":null,\"createBy\":null,\"updateAt\":null,\"updateBy\":null}"
			lastEventId: ""
			origin: "http://localhost:8079"
			type: "message"
			*/
			
			Object.values(this.#addChattingEventListener).forEach(async callBack => {
                new Promise(res => {
                    callBack(content);
                    res();
                })
            });
		});
    }

    set addChattingEventListener({name, callBack}){
        this.#addChattingEventListener[name] = callBack;
    }

    get addChattingEventListener(){
        return this.#addChattingEventListener;
    }

	get lastChattingId(){
		return Number(this.#lastChattingId);
	}

	get lastChatting(){
		return Number(this.#lastChatting);
	}

});

;// CONCATENATED MODULE: ./view/js/handler/IndexedDBHandler.js
/**
 * indexedDB 템플릿 (미완성)
 * @author mou123
 * @constructor
 */
class IndexedDBHandler{
	/**
	 * indexed db에서 사용할 db 식별 명칭
	 */
	#dbName;

	/**
	 * indexed db의 store에서 사용 할 컬럼 정보
	 */
	#columnInfo;

	/**
	 * store 식별 명칭
	 */
	#storeName;

	#db;

	isOpen = false;

	#container;

	#keyPathNameList

	#pkAutoIncrement

	/**
	 * 생성자
	 * @author mozu123
	 * @example 	
	#columnInfo = {
		boTempId:['boTempId','boTempId',{unique : true}],
		boTempName:['boTempName','boTempName'],
		boTempInsertTime:['boTempInsertTime','boTempInsertTime'],
		boTempData:['boTempData','boTempData']
	};
	 */
	constructor({
		dbName = undefined,
		columnInfo = undefined,
		storeName = undefined,
		keyPathNameList = [],
		pkAutoIncrement = true
	}){
		if( ! dbName ){
			throw new Error('dbName is undefined');
		}else if( ! columnInfo ){
			throw new Error('columnInfo is undefined');
		}else if( ! storeName ){
			throw new Error('storeName is undefined')
		}else if( keyPathNameList.length == 0 ){
			throw new Error('keyPathName is undefined')
		}else if(keyPathNameList.filter(e=>columnInfo[e]).length != keyPathNameList.length){
			throw new Error('invalid key path name')
		}else if(pkAutoIncrement == true && keyPathNameList.length > 1){
			throw new Error('multiple key not use autoIncrement : true');
		}
		else{
			this.#dbName = dbName;
			this.#columnInfo = columnInfo;
			this.#storeName = storeName;	
			this.#container = JSON.stringify(
				Object.entries(columnInfo).reduce((obj, [k,v], idx) => {
					obj[k] = undefined;
					return obj;
				}, {})
			)
			this.#keyPathNameList = keyPathNameList;
			this.#pkAutoIncrement = pkAutoIncrement;
		}
	}

	open(){
		return new Promise( (resolve, reject) => {

			const dbOpenRequest = indexedDB.open(this.DB_NAME);
			/**
			 * db open시 기존 버전 정보를 비교하였을 때, 업그레이드가 필요한 경우 동작하는 이벤트를 정의한다.
			 * @author mozu123
			 * @param {Event} e 
			 * @returns 
			 * @see this.upgradeneeded()
			 */
			dbOpenRequest.onupgradeneeded = (e) => this.upgradeneeded(e,{});
			
			/**
			 * db open시 동작하는 이벤트를 정의한다.
			 * @param {Event} e 
			 */
			dbOpenRequest.onsuccess = (e) => {

				this.isOpen = true;
				this.#db = e.target.result;
				let storeName = Object.entries(this.#db.objectStoreNames).find(([idx, storeName]) => storeName == this.DB_STORE_NAME)
				let isNewAddStore = ! storeName;

				let isNeedChangeIndex = false;

				if( ! isNewAddStore){
					let store = this.#db.transaction(this.DB_STORE_NAME, 'readwrite').objectStore(this.DB_STORE_NAME);
					let indexNamesMapper = Object.entries(store.indexNames).reduce((t,[idx,indexName])=>{
						t[indexName]=idx
						return t;
					},{});
					isNeedChangeIndex = 
						Object.entries(indexNamesMapper).findIndex(([name,idx]) => ! this.#columnInfo[name] ) != -1 ||
						Object.entries(this.#columnInfo).findIndex(([k,v])=> ! indexNamesMapper[k] ) != -1
				}

				if( isNewAddStore || isNeedChangeIndex ){
					
					let newVersion = Number(this.#db.version) + 1;
					console.log(this.#storeName, newVersion);
					// 기존 버전과 비교하였을 때 변경사항이 있어서 업그레이드(기존 정보 마이그레이션)가 필요 한 경우
					console.log(dbOpenRequest);
					this.close().then( closeResult => {
						const secondOpenRequest = indexedDB.open(this.DB_NAME, newVersion);
						console.log(secondOpenRequest)
						/**
						 * DB에 변동사항이 생겨 업그레이드가 필요하다면, 기존 db를 닫은 후 다시 열어서 신규 정보를 업그레이드 하는 이벤트가 동작하도록 한다.
						 * @author mozu123
						 * @param {Event} event 
						 * @returns 
						 */
						secondOpenRequest.onupgradeneeded = (event) => this.upgradeneeded(event, {isNewAddStore:isNewAddStore});
						
						/**
						 * 위에 입력된 로직에 따라 업그레이드가 완료된 경우 이 이벤트가 동작한다.
						 * @author mozu123
						 * @param {Event} event 
						 */
						secondOpenRequest.onsuccess = (event) => {
							this.isOpen = true;
							this.#db = event.target.result;
							resolve(this.isOpen);
						}
						secondOpenRequest.onblocked = (e) => {
							console.error(e);
						}
					});
					
				}else{
					resolve(this.isOpen);
				}
			}
			dbOpenRequest.onblocked = (e) => {
				console.log(e);
			}
			/**
			 * db open시 어떤 이유로 인해 오류가 발생할 때 동작하는 이벤트
			 * @param {Event} e 
			 */
			dbOpenRequest.onerror = (e) => {
				console.log(e);
				this.isOpen = false;
				if(e.target.error.name == 'VersionError'){
					//error code
					reject(new Error('VersionError: DB 버전을 수동으로 입력해선 안됩니다.'));
				}else{
					//error code
					console.error(e);
					reject(new Error(`unknownError ${e}`))
				}
			}
		})
	}

	close(){
		return new Promise( resolve => {
			if( ! this.#db || ! this.isOpen){
				resolve(false)
			}
			this.#db.onabort = (e) => console.log(e)
			this.#db.onerror = (e) => console.log(e);
			this.#db.close();
			this.isOpen = false;
			this.#db.onclose = (event) => {
				this.isOpen = false;
				resolve(true)
			}
			resolve(true);
		})
	}

	/**
	 * DB NAME을 가져오는 getter
	 * @author mou123
	 */
	get DB_NAME(){
		return this.#dbName;
	}

	/**
	 * @author mou123
	 */
	get DB_STORE_NAME(){
		return this.#storeName;
	}

	get COLUMN_INFO(){
		return this.#columnInfo;
	}

	get template(){
		return JSON.parse(this.#container);
	}

	/**
	 * indexed DB를 open 할 때 업그레이드 필요 여부에 따라 동작하는 함수를 정의한다.
	 * @author mou123
	 * @param {Event} event 
	 * @param {Object} isNewAddStore : 새롭게 등록되어야 할 store인지 여부 
	 */
	upgradeneeded(event,{isNewAddStore=false}){

		let objectStore;
		// newVersion이 oldVersion과 같지 않고 && oldVersion이 0이 아니며 *(최초 오픈시 0임) && 신규 store를 등록하는 것이 아니라면
		if(event.newVersion != event.oldVersion && event.oldVersion != 0 && ! isNewAddStore){
			objectStore = event.target.transaction.objectStore(this.DB_STORE_NAME);
			let indexNameList = Object.entries(objectStore.indexNames);
			let oldIndexCheckMapper = indexNameList.reduce((t,[idx,indexName])=>{
				t[indexName]=idx
				return t;
			},{});
			
			Object.entries(this.#columnInfo).forEach(([indexName, column])=>{
				if( ! oldIndexCheckMapper[ column[0] ]){
					objectStore.createIndex(...column);
				}
			});

			indexNameList.forEach(([k,indexName])=>{
				if( ! this.#columnInfo[indexName]){
					objectStore.deleteIndex(indexName);
				}
			})
		}else{
			//let keyPath = Object.values(this.#columnInfo).find(column => column.some(e=>e.unique))[0];
			let keyPath = this.#pkAutoIncrement || this.#keyPathNameList.length == 1 ? this.#keyPathNameList[0] : this.#keyPathNameList
			objectStore = event.target.result.createObjectStore(this.DB_STORE_NAME, { keyPath , autoIncrement : this.#pkAutoIncrement });
			Object.entries(this.#columnInfo).forEach( ([indexName, column]) => objectStore.createIndex(...column) );
		}
	}

	/**
	 * indexed db에 데이터를 저장하는 함수
	 * @param {Object} data : indexed db에 저장할 데이터 
	 * @returns {Promise}
	 */
	addItem(data = this.#container){
		return new Promise( (resolve, reject ) => {
			if( ! this.isOpen || ! this.#db){
				reject(new Error('indexedDB가 열려있지 않습니다.'));
			}
			let transaction = this.#db.transaction(this.DB_STORE_NAME, 'readwrite')
			let store = transaction.objectStore(this.DB_STORE_NAME);
			let request = store.put(data);
			request.onsuccess = (e) => {
				transaction.commit();
			}
			request.onerror = (e) => {
				console.log(e)
			}
			transaction.oncomplete = (e)=>{
				if(e.type == 'complete'){
					//alert('임시 저장을 완료하였습니다.');
					//console.log(e.type);
				}
				resolve(e);
			}
			transaction.onerror = (e)=>{
				//error code
				console.log(e)
				reject(new Error('commit error'))
			}
			//return resolve();
		});
	}

	/**
	 * 임시 저장 버튼 클릭시 동작할 함수
	 * @author mozu123
	 * @param {Object} data 
	 */
	/*tempSaveButtonClickEvent(data){
		if( ! data){
			throw new Error('임시 저장 할 데이터가 비어 있습니다.');
		}
		let boTempName = window.prompt('임시 저장 내용을 식별할 명칭을 지정해주세요.\n입력하지 않을시 현재 시간으로 저장됩니다.', new Date().toLocaleString());
		this.addTempItem(data, boTempName);
	}*/

	/**
	 * 임시 저장 목록에서 특정 조건을 만족하는 데이터만 추출할 떄 사용 할 함수 -> 미완성 (안만듬)
	 * @param {Object} param0 : 필터 검색 조건 데이터 
	 * @returns 
	 */
	//getTempFilterList({boTempId, boTempName, boTempInsertTime, pageNum=1, pageSize=5, startDateTime, endDateTime}){}
	
	getItem(key){
		return new Promise( (resolve, reject) => {
			let transaction = this.#db.transaction(this.DB_STORE_NAME, 'readonly');
			let objectStore = transaction.objectStore(this.DB_STORE_NAME);
			let request = objectStore.get(key);
			request.onerror = (event) => {
				console.error(event);
				reject(request);
			}

			request.onsuccess = (event) => {
				resolve(request);
			}
		})
	}

	/**
	 * 임시 저장 목록을 불러오는 함수 pageNum의 기본값은 1, pageSize의 기본값은 5
	 * @author mozu123
	 * @param {Object} param0 : 페이지 정보를 가지고 있는 객체
	 */
	getList({pageNum=1, pageSize=5, callback, readOption = 'readonly'} = {}){
		return new Promise((resolve, reject) => {

			let start = (pageSize * (pageNum - 1));
			let isEnableAdvanced = start != 0; 
			let count = 0;
			let transaction = this.#db.transaction(this.DB_STORE_NAME, readOption)
			let store = transaction.objectStore(this.DB_STORE_NAME);
			let result = {
				data:[],
				event:undefined
			};
			let cursorRequest = store.openCursor()
			cursorRequest.onsuccess = (event) => {
				const cursor = event.target.result;
				if( ! cursor ){
					return ;
				}else if(isEnableAdvanced){
					isEnableAdvanced = ! isEnableAdvanced
					cursor.advance(start);
					return;
				}
				if(callback instanceof Function){
					callback(cursor);
				}
				let value = cursor.value;
				result.data.push(value);
				count += 1;
				
				if(count >= pageSize){
					return;
				}else{
					cursor.continue();
				}
			};
			transaction.oncomplete = (event) => {
				result.event = event;
				return resolve(result);
			}
			transaction.onerror = (e) => reject(e);
			transaction.onabort = (e) => reject(e);
		});
	}

	/**
	 * 임시 저장 목록 중 특정 row를 삭제하는 함수
	 * @author mozu123
	 * @param  {...String} idList : 삭제 할 id 목록
	 * @returns {Promise} result : 삭제 몇건 했는지 count 한 정보를 담은 Object
	 */
	deleteList(...idList){
		if(idList.length == 0){
			return Promise.resolve()
		}
		return new Promise((resolve, reject) => {
			let transaction = this.#db.transaction(this.DB_STORE_NAME, 'readwrite')
			let store = transaction.objectStore(this.DB_STORE_NAME);
			let result = {successCount:0, failedCount:0};
			idList.forEach(id=>{
				let request = store.delete([id]);
				request.onsuccess = (e)=>{
					result.successCount += 1;
					transaction = e.target.transaction;
				}
				request.onerror = (e)=>{
					result.failedCount += 1;
				}
			});
			transaction.commit();
			transaction.oncomplete = (event) => {
				result.event = event;
				return resolve(result);
			}
			transaction.onerror = (e) => reject(e);
			transaction.onabort = (e) => reject(e);
		})
	}

	deleteDatabase(dbName){
		return new Promise( (resolve, reject) => {
			
			let deleteReuqest = indexedDB.deleteDatabase(dbName);
			deleteReuqest.onerror = (event) => {
				console.error(event);
				reject(event);
			}
			deleteReuqest.onsuccess = (event) => {
				console.log(event);
				resolve(event);
			}
		})
	}

}
;// CONCATENATED MODULE: ./view/js/renderer/multipleNoticeBoardViewRenderer.js
/**
 * web용
 */

window.myAPI = myAPI;



















/*
indexedDBHandler.open().then(()=>{
	indexedDBHandler.addItem({
		fileName:'test',
		lastModified:'1111',
		targetId: '1',
		uploadType: 'abcd'
	});
})
*/
const oneDay = 1000 * 60 * 60 * 24;
window.addEventListener('load', async () => {
	const indexedDBHandler = new IndexedDBHandler({
		dbName: 'fileDB-main-page',
		storeName: `s3Memory-main-page`,
		columnInfo: {
			fileName: ['fileName', 'fileName', {unique : true}],
			originFileName: ['originFileName', 'originFileName'],
			fileData : ['fileData', 'fileData'],
			lastModified: ['lastModified', 'lastModified'],
			uploadType: ['uploadType', 'uploadType'],
			roomId: ['roomId', 'roomId'],
			workspaceId: ['workspaceId', 'workspaceId']
		},
		keyPathNameList: ['fileName'],
		pkAutoIncrement : false
	});
	const dbOpenPromise = indexedDBHandler.open();

	const imageOrVideoCallback = async (targetTools) => {
		if(targetTools.hasAttribute('data-is_loading')){
			let uploadLoading = targetTools.querySelector('[data-upload_loading]');
			if( ! uploadLoading){
				uploadLoading = Object.assign(document.createElement('div'), {
					className: 'upload_loading',
					innerHTML: `
					<div class="upload_loading_container">
						<span>컨텐츠를 업로드 중입니다</span>
						<span class="status_text_elipsis" data-status_text_elipsis></span>
					</div>
					`
				})
				uploadLoading.dataset.upload_loading = '';
			}else{
				uploadLoading.className = 'upload_loading'
				uploadLoading.querySelector('[data-status_text_elipsis]').className = 'upload_loading'; 
			}
			targetTools.append(uploadLoading);
			return;
		}


		let fileType;
		if(targetTools.constructor == Image){
			fileType = 'IMAGE';
		}else if(targetTools.constructor == Video){
			fileType = 'VIDEO';
		}else{
			fileType = 'FILE';
		}
		const isHasRememberFile = await new Promise(resolve => {
			/*if(targetTools.constructor == Resources){
				resolve({result: undefined});
			}*/
			dbOpenPromise.then(() => {
				new Promise(res=>{
					indexedDBHandler.getList({
						pageNum : 1, 
						pageSize : 99999, 
						readOption: 'readwrite', 
						callBack: (cursor)=>{
							if(cursor.value.lastModified < new Date().getTime() - oneDay){
								cursor.delete();
							}
						}
					}).then(result=>{
						res();
						/*let deleteTargetList = result.data.filter(e=>{
							let lastModified = parseInt(e.lastModified);
							return lastModified - oneDay <= new Date().getTime() - oneDay;
						}).map(e=>e.fileName);
						indexedDBHandler.deleteList(deleteTargetList).then((deleteResult) => {
							res();
						});*/
					}).catch((err)=>{
						console.error(err)
						res();
					})	
				}).then(() => {
					indexedDBHandler.getItem(targetTools.dataset.new_file_name).then(result=>{
						resolve(result);
					});
				})
			})
		})

		if(isHasRememberFile.result){
			let url = window.URL.createObjectURL(isHasRememberFile.result.fileData, targetTools.dataset.content_type)
			targetTools.dataset.url = url;
			if(targetTools.image){
				targetTools.image.src = url;
			}else if(targetTools.video){
				targetTools.video.src = url;
			}else{
				//targetTools.resources.data = url;
				targetTools.resourcesUrl = url;
			}
			return;
		}

		let startPromise = new Promise(resolve => {

			let {size, rank, rankText} = common.shortenBytes(targetTools.dataset.size);

			if(size >= 10 && rank >= 2){
				let filePreview = targetTools.querySelector('[data-file_preview]');
				if( ! filePreview){
					filePreview = Object.assign(document.createElement('div'), {
						className: 'file_preview',
						innerHTML: `
						<div class="file_preview_container" data-file_preview_container>
							<div>10MB 이상의 파일은 당신의 데이터를 위해 미리보기를 제공하지 않습니다.</div>
							<div>미리보기를 클릭시 기능 제공을 위해 임시 저장소에 저장을 시작하며, 이는 추후 자동 삭제의 대상이 됩니다.</div>
							<div>이 파일의 용량 : ${size}${rankText}</div>
							<button class="file_preview_button" data-file_preview_button type="button">미리보기</button>
						</div>
						`
					});
					filePreview.dataset.file_preview = '';
				}else{
					filePreview.querySelector('[data-file_preview_container]').className = 'file_preview_container'
					filePreview.querySelector('[data-file_preview_button]').className = 'file_preview_button'; 
					filePreview.className = 'file_preview';
				}
				filePreview.style.display = 'none';
				Resources.resourcesCallback = ({status, resources}) => {
					if(status == 'error'){
						filePreview.style.display = '';
					}
				}
				filePreview.onclick = (event) => {
					event.stopPropagation();
				}
				filePreview.dataset.visibility_not = '';

				targetTools.append(filePreview)

				let filePreviewButton = filePreview.querySelector('.file_preview_button');
				//document.body.onclick = (event) =>{ event}
				filePreviewButton.onclick = (event) => {

					event.stopPropagation();
					filePreviewButton.textContent = '';
					filePreviewButton.className = 'loading_rotate';
					resolve(filePreview);
				}
			}else{
				resolve();
			}
		});

		startPromise.then((filePreview) => {

			let getSignData = `${RoomHandler.roomId}:${WorkspaceHandler.workspaceId}:${targetTools.dataset.new_file_name}:${accountHandler.accountInfo.accountName}`
			s3EncryptionUtil.callS3PresignedUrl(window.myAPI.s3.generateGetObjectPresignedUrl, getSignData, {uploadType : targetTools.dataset.upload_type, fileType})
			.then( (result) => {
				if(! result){
					return;
				}
				let {data, encDncKeyPair} = result;
				let totalLen = 0;
				let size = parseInt(targetTools.dataset.size);
				let progress = Object.assign(document.createElement('progress'), {
					max: 100,
					value : 0
				});
				if(filePreview){
					let container = filePreview.querySelector('.file_preview_container');
					container.append(progress)
				}
				return Promise.all([
					s3EncryptionUtil.convertBase64ToBuffer(data.encryptionKey).then( async (buffer) => {
						return s3EncryptionUtil.decryptMessage(encDncKeyPair.privateKey, buffer, s3EncryptionUtil.secretAlgorithm)
							.then(buf=>String.fromCharCode(...new Uint8Array(buf)))
					}),
					s3EncryptionUtil.convertBase64ToBuffer(data.encryptionMd).then( async (buffer) => {
						return s3EncryptionUtil.decryptMessage(encDncKeyPair.privateKey, buffer, s3EncryptionUtil.secretAlgorithm)
							.then(buf=>String.fromCharCode(...new Uint8Array(buf)))
					})
				]).then( async ([k,m]) => {
					return fetch(data.presignedUrl, {
						method:"GET",
						headers: {
							'Content-Encoding' : 'base64',
							'Content-Type' : 'application/octet-stream',
							'x-amz-server-side-encryption-customer-algorithm': 'AES256',
							'x-amz-server-side-encryption-customer-key': k,
							'x-amz-server-side-encryption-customer-key-md5': m,
						}
					}).then(async response=> {
						if(response.status != 200 && response.status != 201){
							throw new Error('s3 connect failed')
						}
						return response.body;
					}).then((body) => {
						const reader = body.getReader();
						return new ReadableStream(
							{
								start(controller) {
									return pump();
									function pump() {
										return reader.read().then(({ done, value }) => {
											totalLen += value?.length || 0;
											progress.value = (totalLen / size) * 100 
											// When no more data needs to be consumed, close the stream
											if (done) {
												controller.close();
												return;
											}
											// Enqueue the next data chunk into our target stream
											controller.enqueue(value);
											return pump();
										});
									}
								},
							}
						);
						/*
						let newBlob = new Blob([buffer], { type: imageEditor.dataset.content_type });
						let imgUrl = window.URL.createObjectURL(newBlob);
						*/
					})
					.then(stream => new Response(stream))
					.then(res => res.blob())
					.then(async blob => {
						let newBlob = new Blob([blob], { type: targetTools.dataset.content_type });
						return dbOpenPromise.then( async () => {
							return indexedDBHandler.addItem({
								fileName: targetTools.dataset.new_file_name,
								originFileName: targetTools.dataset.name,
								fileData: newBlob,
								lastModified: new Date().getTime(),
								uploadType: targetTools.dataset.upload_type,
								roomId: RoomHandler.roomId,
								workspaceId: WorkspaceHandler.workspaceId
							}).then(()=>{
								return window.URL.createObjectURL(newBlob)
							})
						})
					})
					.then(url => {
						//targetTools.dataset.url = url;
						if(targetTools.image){
							targetTools.image.src = url;
						}else if(targetTools.video){
							targetTools.video.src = url;
						}else{
							//targetTools.resources.data = url;
							targetTools.resourcesUrl = url;
						}

						if(filePreview){
							filePreview.replaceChildren();
							filePreview.parentElement.removeChild(filePreview);
							filePreview.remove();
						}
					})
					.catch(err=>{
						console.error(err);
						console.error(err.message)
					})
				
				})
			})

		})
		
	}

	Image.customImageCallback = (imageEditor) => imageOrVideoCallback(imageEditor)
	Video.customVideoCallback = (videoEditor) => imageOrVideoCallback(videoEditor)
	Resources.customResourcesCallback = (resourcesEditor) => imageOrVideoCallback(resourcesEditor)
});	

const visibleObserver = new IntersectionObserver((entries, observer) => {
	entries.forEach(entry =>{
		let {isIntersecting, target} = entry;
		if(target.hasAttribute('data-visibility_not')){
			return;
		}
		if (isIntersecting){
			target.style.visibility = '';
			target.style.opacity = '';
			target.dataset.visibility = 'v';
		}else{
			target.style.visibility = 'hidden';
			target.style.opacity = 0;
			target.dataset.visibility = 'h';
		}
	})
}, {
	threshold: 0.01,
	root: document
});
new MutationObserver( (mutationList, observer) => {
	mutationList.forEach((mutation) => {
		let {addedNodes, removedNodes} = mutation;
		new Promise(resolve=> {
			addedNodes.forEach(async e => {
				if(e.nodeType !== Node.ELEMENT_NODE || (e.nodeType === Node.ELEMENT_NODE && e.hasAttribute('data-is_not_visible_target'))){
					return;
				}
				new Promise(res=>{
					visibleObserver.observe(e);
					res();
				})
			})
			resolve();
		})
		new Promise(resolve=> {
			removedNodes.forEach(async e => {
				if(e.nodeType !== Node.ELEMENT_NODE || (e.nodeType === Node.ELEMENT_NODE && e.hasAttribute('data-is_not_visible_target'))){
					return;
				}
				new Promise(res=>{
					visibleObserver.unobserve(e);
					res();
				})
			})
			resolve();
		})
	})
}).observe(document, {
	childList: true,
	subtree: true
})

window.addEventListener("DOMContentLoaded", (event) => {
	let workspaceIdResolve;
	let workspaceIdPromise = new Promise(resolve=>{
		workspaceIdResolve = resolve;
	})
	window.myAPI.getWorkspaceId().then(workspaceId=>{
		if(workspaceId != undefined){
			workspaceIdResolve(workspaceId);
		}
		window.myAPI.event.electronEventTrigger.addElectronEventListener('workspaceChange', event => {
			let newWorkspaceId = event.workspaceId
			if(workspaceId == newWorkspaceId){
				return;
			}
			if(newWorkspaceId != undefined){
				workspaceIdResolve(newWorkspaceId)
			}
			//event.workspaceId
		})
	})
	workspaceIdPromise.then(workspaceId => {
		document.querySelector('#main').append(
            NoticeBoardDetail.element
		)
        NoticeBoardDetail.element.dataset.is_resize = false;
        NoticeBoardDetail.element.grow = 1;

        window.myAPI.event.electronEventTrigger.addElectronEventListener('noticeBoardChange', event => {
            NoticeBoardHandler.noticeBoardId = event.noticeBoardId;
        })

		window.myAPI.stream.initWorkspaceStream({workspaceId});
	})
});

/******/ })()
;
//# sourceMappingURL=multipleNoticeBoardViewRenderer.js.map