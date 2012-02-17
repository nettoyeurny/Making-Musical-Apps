//
//  GTViewController.h
//  GuitarTuner
//

#import <UIKit/UIKit.h>

#import "PdDispatcher.h"
#import "PitchView.h"

@interface GTViewController : UIViewController<PdListener> {
    PdDispatcher *dispatcher;
    void *patch;
}

@property (strong, nonatomic) IBOutlet UILabel *pitchLabel;
@property (strong, nonatomic) IBOutlet PitchView *pitchView;

- (IBAction)playE:(id)sender;
- (IBAction)playA:(id)sender;
- (IBAction)playD:(id)sender;
- (IBAction)playG:(id)sender;
- (IBAction)playB:(id)sender;
- (IBAction)playE2:(id)sender;

- (void)receiveFloat:(float)value fromSource:(NSString *)source;

@end
