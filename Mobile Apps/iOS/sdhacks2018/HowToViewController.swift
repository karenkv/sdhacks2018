//
//  HowToViewController.swift
//  sdhacks2018
//
//  Created by Rebecca Leung on 10/13/18.
//  Copyright © 2018 best team. All rights reserved.
//

import UIKit

class HowToViewController: UIViewController {

    // Icons made by "https://www.flaticon.com/authors/smashicons" Smashicons
    
    @IBOutlet weak var InstallLabel: UILabel!
    @IBOutlet weak var NextButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NextButton.setButtonBorder()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
