//
//  StringExtension.swift
//  sdhacks2018
//
//  Created by Rebecca Leung on 10/13/18.
//  Copyright © 2018 best team. All rights reserved.
//

import UIKit

extension UIButton {
    func setButtonBorder() {
        self.layer.cornerRadius = 5
        self.layer.borderWidth = 1
        self.layer.borderColor = UIColor.black.cgColor
    }
}
