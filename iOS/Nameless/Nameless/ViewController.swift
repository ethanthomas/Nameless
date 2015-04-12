//
//  ViewController.swift
//  Nameless
//
//  Created by Ethan on 4/11/15.
//  Copyright (c) 2015 Nameless. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    let context = CIContext(options: nil)
    
    override func viewDidLoad() {
        var connectMeButton: UIButton = UIButton(frame: CGRectMake(self.view.frame.width / 2, self.view.frame.height / 2, 290, 90));
        connectMeButton.setTitle("Connect Me", forState: nil);
        connectMeButton.tintColor = UIColor.greenColor()
        connectMeButton.setTitleColor(UIColor.greenColor(), forState: nil)
        self.view.addSubview(connectMeButton);
        
//        let testFrame : CGRect = CGRectMake(0,200,320,200)
//        var testView : UIView = UIView(frame: testFrame)
//        testView.backgroundColor = UIColor(red: 0.5, green: 0.5, blue: 0.5, alpha: 1.0)
//        testView.alpha=0.5
//        self.view.addSubview(testView)
        super.viewDidLoad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

