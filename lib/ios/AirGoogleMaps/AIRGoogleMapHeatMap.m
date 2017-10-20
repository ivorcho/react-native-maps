//
//  AirGoogleMapHeatMap.m
//  Created by Petri Louhelainen on 02/10/2017.
//

#import "AIRGoogleMapHeatMap.h"
#import "AIRMapCoordinate.h"
#import "GMUWeightedLatLng.h"

@implementation AIRGoogleMapHeatMap {
  NSNumber *_maxIntensity;
  GMUGradient *_gradient;
}

- (instancetype)init {
  if ((self = [super init])) {
    _gradientColors = @[[UIColor colorWithRed:140.f / 255.f green:200.f / 255.f blue:0 alpha:1],
                        [UIColor colorWithRed:1.0f green:0 blue:0 alpha:1]];
    _gradientSteps = @[ @0.3f, @1.0f ];
    _defaultColor = [UIColor clearColor];
  }
  return self;
}

- (void)setZIndex:(int)zIndex
{
  _zIndex = zIndex;
  _tileLayer.zIndex = zIndex;
}

- (void)setOpacity:(float)opacity
{
  _opacity = opacity;
  _tileLayer.opacity = opacity;
}

- (void)setRadius:(int)radius
{
  _radius = radius;
  _tileLayer.radius = radius;
}

- (void)setGradientSteps:(NSArray<NSNumber *> *)gradientSteps
{
  _maxIntensity = gradientSteps.lastObject;
  NSMutableArray *newGradientSteps = [NSMutableArray arrayWithCapacity:gradientSteps.count];
  for (NSNumber *step in gradientSteps) {
    float relativeStep = step.floatValue/_maxIntensity.floatValue;
    [newGradientSteps addObject: [NSNumber numberWithFloat:relativeStep]];
  }
  _gradientSteps = [newGradientSteps copy];
  
  _gradient = [[GMUGradient alloc] initWithColors:_gradientColors
                                      startPoints:_gradientSteps
                                     defaultColor:_defaultColor
                                     colorMapSize:1000];
  
  if (_tileLayer != NULL) {
    _tileLayer.maxIntensity = _maxIntensity;
    _tileLayer.gradient = _gradient;
    [_tileLayer clearTileCache];
  }
}

- (void)setGradientColors:(NSArray<UIColor *> *)gradientColors
{
  _gradientColors = gradientColors;
  _gradient = [[GMUGradient alloc] initWithColors:_gradientColors
                                      startPoints:_gradientSteps
                                     defaultColor:_defaultColor
                                     colorMapSize:1000];
  if (_tileLayer != NULL) {
    _tileLayer.gradient = _gradient;
    [_tileLayer clearTileCache];
  }
}

- (void)setDefaultColor:(UIColor *)defaultColor
{
  _defaultColor = defaultColor;
  _gradient = [[GMUGradient alloc] initWithColors:_gradientColors
                                      startPoints:_gradientSteps
                                     defaultColor:_defaultColor
                                     colorMapSize:1000];
  if (_tileLayer != NULL) {
    _tileLayer.gradient = _gradient;
    [_tileLayer clearTileCache];
  }
}

- (void)setCoordinates:(NSArray<AIRMapCoordinate *> *)coordinates
{
  _coordinates = coordinates;
  
  NSMutableArray<GMUWeightedLatLng *> * data = [NSMutableArray arrayWithCapacity:coordinates.count];
  for(int i = 0; i < coordinates.count; i++)
  {
    [data addObject:[[GMUWeightedLatLng alloc] initWithCoordinate:coordinates[i].coordinate intensity:1.0]];
  }
  
  if (_tileLayer == NULL) {
    _tileLayer = [[GMUHeatmapTileLayer alloc] init];
    _tileLayer.radius = _radius;
    _tileLayer.opacity = _opacity;
    _tileLayer.maxIntensity = _maxIntensity;
    _tileLayer.gradient = _gradient;
    _tileLayer.weightedData = data;
  } else {
    NSLog(@"Clearing tile data");
    _tileLayer.weightedData = data;
    [_tileLayer clearTileCache];
  }
}

@end
