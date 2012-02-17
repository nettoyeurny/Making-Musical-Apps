//
//  GTAppDelegate.h
//  GuitarTuner
//

#import <UIKit/UIKit.h>
#import "PdAudioController.h"

@class GTViewController;

@interface GTAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) GTViewController *viewController;

@property (strong, nonatomic, readonly) PdAudioController *audioController;

@end
