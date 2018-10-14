//
//  LoginOptionsViewController.swift
//  sdhacks2018
//
//  Created by Rebecca Leung on 10/13/18.
//  Copyright © 2018 best team. All rights reserved.
//

import UIKit

class LoginOptionsViewController: UIViewController {

    @IBOutlet weak var SnapchatLoginButton: UIButton!
    @IBOutlet weak var emailLoginButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        SnapchatLoginButton.setButtonBorder()
        emailLoginButton.setButtonBorder()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
