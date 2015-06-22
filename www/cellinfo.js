var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var utils = require('cordova/utils');
var exec = require('cordova/exec');


var CellInfo = function() {
    var me = this;

    channel.onCordovaReady.subscribe(function() {
        me.getNetworkType(function(response) {
            me.available = true;
            me.networkType = response;
        }, function(e) {
            me.available = false;
            utils.alert('[ERROR] Error initializing Cordova: ' + e);
        });
    });
};


CellInfo.prototype.getNeighboringCellInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getNeighboringCellInfo', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getNeighboringCellInfo', []);
};

CellInfo.prototype.getPrimaryCellInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getPrimaryCellInfo', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getPrimaryCellInfo', []);
};

CellInfo.prototype.getNetworkType = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'CellInfo.getNetworkType', arguments);
    exec(successCallback, errorCallback, 'CellInfo', 'getNetworkType', []);
};

module.exports = new CellInfo();
