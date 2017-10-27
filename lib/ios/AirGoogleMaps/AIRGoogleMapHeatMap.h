//
//  AirGoogleMapHeatMap.h
//  Created by Petri Louhelainen on 02/10/2017.

#import <Foundation/Foundation.h>
#import <GoogleMaps/GoogleMaps.h>
#import "GMUHeatmapTileLayer.h"
#import "AIRMapCoordinate.h"

@interface AIRGoogleMapHeatMap : UIView

@property (nonatomic, strong) GMUHeatmapTileLayer *tileLayer;
@property (nonatomic, strong) NSArray<NSDictionary *> *coordinates;
@property (nonatomic, assign) int zIndex;
@property (nonatomic, assign) float opacity;
@property (nonatomic, assign) int radius;
@property (nonatomic, strong) NSArray<NSNumber *> *gradientSteps;
@property (nonatomic, strong) NSArray<UIColor *> *gradientColors;
@property (nonatomic, assign) UIColor *defaultColor;

@end

