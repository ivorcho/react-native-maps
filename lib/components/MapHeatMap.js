import PropTypes from 'prop-types';
import React from 'react';

import {
  ViewPropTypes,
} from 'react-native';

import decorateMapComponent, {
  USES_DEFAULT_IMPLEMENTATION,
  NOT_SUPPORTED,
} from './decorateMapComponent';

const propTypes = {
  ...ViewPropTypes,

  /**
   * The url template of the tile server. The patterns {x} {y} {z} will be replaced at runtime
   * For example, http://c.tile.openstreetmap.org/{z}/{x}/{y}.png
   */
  urlTemplate: PropTypes.string.isRequired,

  /**
   * The order in which this tile overlay is drawn with respect to other overlays. An overlay
   * with a larger z-index is drawn over overlays with smaller z-indices. The order of overlays
   * with the same z-index is arbitrary. The default zIndex is -1.
   *
   * @platform android
   */
  zIndex: PropTypes.number,
};

class MapHeatMap extends React.Component {
  render() {
    const AIRMapHeatMap = this.getAirComponent();
    return (
      <AIRMapHeatMap
        {...this.props}
      />
    );
  }
}

MapHeatMap.propTypes = propTypes;

module.exports = decorateMapComponent(MapHeatMap, {
  componentType: 'HeatMap',
  providers: {
    google: {
      ios: NOT_SUPPORTED,
      android: USES_DEFAULT_IMPLEMENTATION,
    },
  },
});
