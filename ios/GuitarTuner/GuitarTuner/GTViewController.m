//
//  GTViewController.m
//  GuitarTuner
//

#import "GTViewController.h"

@implementation GTViewController

@synthesize pitchLabel;
@synthesize pitchView;

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

void fiddle_tilde_setup();

- (void)viewDidLoad {
    [super viewDidLoad];
    pitchView.centerPitch = 45;
    pitchLabel.text = @"A-String";
    dispatcher = [[PdDispatcher alloc] init];
    [dispatcher addListener:self forSource:@"pitch"];
    [PdBase setDelegate:dispatcher];
    fiddle_tilde_setup();
    patch = [PdBase openFile:@"tuner.pd" path:[[NSBundle mainBundle] resourcePath]];
    if (!patch) {
        NSLog(@"Failed to open patch!");
        // Gracefully handle failure...
    }
}

- (void)viewDidUnload {
    [super viewDidUnload];
    [PdBase closeFile:patch];
    [PdBase setDelegate:nil];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

#pragma mark - button callbacks

- (void)playNote:(int)n {
    pitchView.centerPitch = n;
    [PdBase sendFloat:n toReceiver:@"midinote"];
    [PdBase sendBangToReceiver:@"trigger"];
}

- (IBAction)playE:(id)sender {
    pitchLabel.text = @"E-string (low)";
    [self playNote:40];
}

- (IBAction)playA:(id)sender {
    pitchLabel.text = @"A-string";
    [self playNote:45];
}

- (IBAction)playD:(id)sender {
    pitchLabel.text = @"D-string";
    [self playNote:50];
}

- (IBAction)playG:(id)sender {
    pitchLabel.text = @"G-string";
    [self playNote:55];
}

- (IBAction)playB:(id)sender {
    pitchLabel.text = @"B-string";
    [self playNote:59];
}

- (IBAction)playE2:(id)sender {
    pitchLabel.text = @"E-string (high)";
    [self playNote:64];
}

#pragma mark - PdListener callbacks

-(void)receiveFloat:(float)value fromSource:(NSString *)source {
    pitchView.currentPitch = value;
}

@end
