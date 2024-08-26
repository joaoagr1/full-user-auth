package com.example.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<DocumentValidation, String> {

    @Override
    public void initialize(DocumentValidation constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty() || isCpf(value) || isCnpj(value);
    }

    private boolean isCpf(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }

        int d1 = 0, d2 = 0;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;

        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Character.getNumericValue(cpf.charAt(nCount - 1));
            d1 += (11 - nCount) * digitoCPF;
            d2 += (12 - nCount) * digitoCPF;
        }

        resto = d1 % 11;
        digito1 = (resto < 2) ? 0 : 11 - resto;

        d2 += 2 * digito1;
        resto = d2 % 11;
        digito2 = (resto < 2) ? 0 : 11 - resto;

        nDigResult = String.valueOf(digito1) + digito2;
        String nDigVerific = cpf.substring(cpf.length() - 2);

        return nDigVerific.equals(nDigResult);
    }

    private boolean isCnpj(String cnpj) {
        cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "");

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) {
            return false;
        }

        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
                cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
                cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
                cnpj.equals("88888888888888") || cnpj.equals("99999999999999") ||
                cnpj.length() != 14) {
            return false;
        }

        int sm = 0, peso = 2;
        for (int i = 11; i >= 0; i--) {
            int num = cnpj.charAt(i) - '0';
            sm += num * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }

        int r = sm % 11;
        char dig13 = (r == 0 || r == 1) ? '0' : (char) ((11 - r) + '0');

        sm = 0;
        peso = 2;
        for (int i = 12; i >= 0; i--) {
            int num = cnpj.charAt(i) - '0';
            sm += num * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }

        r = sm % 11;
        char dig14 = (r == 0 || r == 1) ? '0' : (char) ((11 - r) + '0');

        return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
    }
}
