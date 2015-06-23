var argscheck = require('cordova/argscheck');
var exec = require('cordova/exec');


var CellInfo = function() {
};

CellInfo.getNeighboringCellInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getNeighboringCellInfo', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getNeighboringCellInfo', []);
};

CellInfo.getPrimaryCellInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getPrimaryCellInfo', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getPrimaryCellInfo', []);
};

module.exports = CellInfo;
