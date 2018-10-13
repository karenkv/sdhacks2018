//
//  UITextFieldExtension.swift
//  sdhacks2018
//
//  Created by Rebecca Leung on 10/13/18.
//  Copyright © 2018 best team. All rights reserved.
//

import UIKit

extension UITextField {
    func addPadding() {
        let paddingView = UIView(frame: CGRect(x:0, y:0, width:5, height:self.frame.height))
        self.leftView = paddingView
        self.leftViewMode = UITextFieldViewMode.always
    }
}
