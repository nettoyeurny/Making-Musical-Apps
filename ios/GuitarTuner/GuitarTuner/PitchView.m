//
//  PitchView.m
//  GuitarTuner
//

#import <math.h>
#import "PitchView.h"

@implementation PitchView

@synthesize centerPitch = _centerPitch;
@synthesize currentPitch = _currentPitch;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)setCenterPitch:(float)centerPitch {
    _centerPitch = centerPitch;
    [self setNeedsDisplay];
}

-(void)setCurrentPitch:(float)currentPitch {
    _currentPitch = currentPitch;
    [self setNeedsDisplay];
}

-(void)drawRect:(CGRect)rect {
    float halfWidth = self.frame.size.width / 2;
    float height = self.frame.size.height;
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetLineWidth(context, 6.0);
    CGContextSetStrokeColorWithColor(context, [UIColor greenColor].CGColor);
    CGContextMoveToPoint(context, halfWidth, 0);
    CGContextAddLineToPoint(context, halfWidth, height);
    CGContextStrokePath(context);
    
    float dx = (self.currentPitch - self.centerPitch) / 2;
    if (-1 < dx && dx < 1) {
        CGContextSetLineWidth(context, 2.0);
        CGContextSetStrokeColorWithColor(context, [UIColor blueColor].CGColor);
    } else {
        CGContextSetLineWidth(context, 8.0);
        CGContextSetStrokeColorWithColor(context, [UIColor redColor].CGColor);
        dx = (dx < 0) ? -1 : 1;
    }
    float phi = dx * M_PI_4;
    CGContextMoveToPoint(context, halfWidth, height);
    CGContextAddLineToPoint(context, halfWidth + sin(phi) * height * 0.9,
                            height - cos(phi) * height * 0.9);
    CGContextStrokePath(context);
}

@end
