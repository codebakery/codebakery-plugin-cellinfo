var argscheck = require('cordova/argscheck');
var exec = require('cordova/exec');

if (platform.id === 'android') {
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
} else {
    var CellInfo = false;
}

module.exports = CellInfo;
