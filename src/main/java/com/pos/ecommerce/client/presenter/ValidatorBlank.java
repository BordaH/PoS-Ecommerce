package com.pos.ecommerce.client.presenter;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.user.client.Window;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;

import java.util.ArrayList;
import java.util.List;

public class ValidatorBlank implements org.gwtbootstrap3.client.ui.form.validator.Validator<String> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String s) {
        List<EditorError> result = new ArrayList<EditorError>();
        String valueStr = s == null ? "" : s.toString();
        if (valueStr.isEmpty()) {
            result.add(new BasicEditorError(editor, s, "Complete este campo"));
        }
        return result;
    }
}
