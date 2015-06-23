# codebakery-plugin-cellinfo

This plugin defines `window.CellInfo` object, which adds methods for getting
primary and neighboring cell information as well as network type.

## CellInfo object

```javascript
{
    'cid': Number,          // cell ID in GSM
    'lac': Number,          // LAC in GSM
    'psc': Number,          // primary scrabling code for UMTS, -1 for GSM
    'networkType': String,  // network type string, e.g. 'GSM' or 'UMTS'
    'rssi': Number          // received signal strength
}
```

## Methods

- window.CellInfo.getNeighboringCellInfo - returns list of *CellInfo* objects
- window.CellInfo.getPrimaryCellInfo - returns *CellInfo* object

### Supported Platforms

- Android
- iOS

### Android Installation

API level 3+ supported.

Requires `ACCESS_COARSE_LOCATION` permission.
