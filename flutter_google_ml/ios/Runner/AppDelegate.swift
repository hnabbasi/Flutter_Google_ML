import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    
        let controller = window?.rootViewController as! FlutterViewController
        
        let channel = FlutterMethodChannel(name: "com.intelliabb.flutter_google_ml", binaryMessenger: controller.binaryMessenger)
        
        channel.setMethodCallHandler(methodCallHandler)
        
        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
        
    }
    
    private func methodCallHandler(call: FlutterMethodCall, result: @escaping FlutterResult) -> Void {
        
        guard call.method == "getScannedText" else {
            result(FlutterMethodNotImplemented)
            return
        }
        getScannedText(result: result)
    }
   
    private func getScannedText(result: @escaping FlutterResult) -> Void {
        
        let controller = window?.rootViewController as! FlutterViewController
        var svc:ScanViewController
        
        if #available(iOS 13.0, *) {
            svc = controller.storyboard?.instantiateViewController(identifier: "textscan") as! ScanViewController
        } else {
            svc = ScanViewController()
        }
        
        svc.result = result
        
        controller.present(svc, animated: false, completion: nil)
    }
}
