//
//  SignUpViewController.swift
//  sdhacks2018
//
//  Created by Rebecca Leung on 10/13/18.
//  Copyright © 2018 best team. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {

    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var signupButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        emailTextField.addPadding()
        passwordTextField.addPadding()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
