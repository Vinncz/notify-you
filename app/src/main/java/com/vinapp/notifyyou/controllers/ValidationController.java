package com.vinapp.notifyyou.controllers;

public class ValidationController {

    public static boolean isBlank (String _s) {
        int length = _s.length();
        int left = 0;
        while (left < length) {
            int codepoint = _s.codePointAt(left);
            if (codepoint != ' ' && codepoint != '\t' && !Character.isWhitespace(codepoint)) {
                break;
            }
            left += Character.charCount(codepoint);
        }

        int indexOfNonWhitespace = left;
        return indexOfNonWhitespace == _s.length();
    }



    public static boolean isLessThanNCharacters (String _s, int _length) {
        return _s.length() < _length;
    }

    public static boolean isLongerThanNCharacters (String _s, int _length) {
        return _s.length() > _length;
    }

    public static boolean isEqualsToNCharacters (String _s, int _length) {
        return _s.length() == _length;
    }

    public static boolean isLessThanOrEqualsToNCharacters (String _s, int _length) {
        return _s.length() <= _length;
    }

    public static boolean isLongerThanOrEqualsToNCharacters (String _s, int _length) {
        return _s.length() >= _length;
    }
}
