import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn } from '@angular/forms';

/** A hero's name can't match the given regular expression */
export function checkEmailValidator(nameRe: RegExp): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const checkEmail = nameRe.test(control.value);
    return checkEmail ? null : {checkEmail: {value: control.value}};
  };
}

@Directive({
  selector: '[appCheckEmail]',
  providers: [{provide: NG_VALIDATORS, useExisting: CheckEmailValidatorDirective, multi: true}]
})
export class CheckEmailValidatorDirective implements Validator {
  @Input('appCheckEmail') checkEmail = '';

  validate(control: AbstractControl): ValidationErrors | null {
    return this.checkEmail ? checkEmailValidator(new RegExp(this.checkEmail, 'i'))(control)
                              : null;
  }
}



/*
Copyright Google LLC. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at https://angular.io/license
*/