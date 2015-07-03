# codebakery-plugin-cellinfo

This plugin defines `window.CellInfo` object, which adds methods for getting
primary and neighboring cell information as well as network type.

## CellInfoData object

```javascript
{
    'cid': Number,          // cell ID in GSM
    'lac': Number,          // LAC in GSM
    'psc': Number,          // primary scrabling code for UMTS, -1 for GSM
    'networkType': String,  // network type string, e.g. 'GSM' or 'UMTS'
    'rssi': Number           // received signal strength in dBm
}
```

## Methods

- window.CellInfo.getNeighboringCellInfo - returns list of **CellInfoData** objects
- window.CellInfo.getPrimaryCellInfo - returns **CellInfoData** object

### Supported Platforms

- Android

### Android Installation

API level 7+ supported.

Requires `ACCESS_COARSE_LOCATION` permission.
