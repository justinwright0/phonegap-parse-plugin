#import <Cordova/CDV.h>
#import "AppDelegate.h"

@interface CDVParsePlugin: CDVPlugin{
    NSString* startCallbackID;
}

@property (nonatomic, strong) NSString* startCallbackID;
@property (nonatomic, strong) NSString* notificationCallbackID;

- (void)initialize: (CDVInvokedUrlCommand*)command;
- (void)getInstallationId: (CDVInvokedUrlCommand*)command;
- (void)getInstallationObjectId: (CDVInvokedUrlCommand*)command;
- (void)getSubscriptions: (CDVInvokedUrlCommand *)command;
- (void)subscribe: (CDVInvokedUrlCommand *)command;
- (void)unsubscribe: (CDVInvokedUrlCommand *)command;

@end

@interface AppDelegate (CDVParsePlugin)
@end
