var argscheck = require('cordova/argscheck');
var exec = require('cordova/exec');

var supportedPlatforms = [
  'android'
];

var CellInfo = {
  isAvailable: (supportedPlatforms.indexOf(platform.id) > -1),
  getNeighboringCellInfo: function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getNeighboringCellInfo', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getNeighboringCellInfo', []);
  },
  getPrimaryCellInfo: function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getPrimaryCellInfo', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getPrimaryCellInfo', []);
  }
};

module.exports = CellInfo;
