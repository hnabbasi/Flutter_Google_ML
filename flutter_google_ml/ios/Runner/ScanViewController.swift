//
//  ScanViewController.swift
//  Runner
//
//  Created by Hussain on 9/6/20.
//

import Foundation
import Flutter
import MLKit

class ScanViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var resultLabel: UILabel!
    
    @IBAction func doneTouched(_ sender: Any) {
        done()
    }
    
    var result: FlutterResult?
    private lazy var textRecognizer = TextRecognizer.textRecognizer()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        startCamera()
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
        
        let tempImage = info[UIImagePickerController.InfoKey.originalImage] as! UIImage
        imageView.image  = tempImage
        processImage(image: tempImage)
    }
    
    private func processImage(image: UIImage) {
        let visionImage = VisionImage(image: image)
        textRecognizer.process(visionImage) { features, error in
          self.processResult(from: features, error: error)
        }
        doneProcessing()
    }
    
    func processResult(from text: Text?, error: Error?) {

      guard error == nil, let text = text else {
        let errorString = error?.localizedDescription
        print("Text recognizer failed with error: \(String(describing: errorString))")
        return
      }
        resultLabel!.text! = ""

      for block in text.blocks {
        for line in block.lines {
          for element in line.elements {
            resultLabel!.text! += element.text
          }
        }
      }
    }
    
    private func doneProcessing() {
        dismiss(animated: false) {
            self.result!(self.resultLabel!.text)
        }
        dismiss(animated: true, completion: nil)
    }
    
    private func done() {
        dismiss(animated: true) {
            self.result!("No text recognized")
        }
    }
}
