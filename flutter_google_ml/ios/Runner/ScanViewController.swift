//
//  ScanViewController.swift
//  Runner
//
//  Created by Hussain on 9/6/20.
//

import Foundation
import Flutter

class ScanViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    var result: FlutterResult?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // TODO: Launch camera
//        startCamera()
        // Get camera result
        // process the result
        // return the result
    }
    
    private func startCamera() {
        let vc = UIImagePickerController()
        vc.allowsEditing = true
        vc.delegate = self
        
        #if targetEnvironment(simulator)
        vc.sourceType = .photoLibrary
        #else
        vc.sourceType = .camera
        vc.cameraFlashMode = .auto
        #endif
        
        present(vc, animated: true)
    }
    
    internal func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        processImage()
    }
    
    private func processImage() {
        doneProcessing()
    }
    
    private func doneProcessing() {
        dismiss(animated: true) {
            self.result!("Image processed")
        }
    }
}
