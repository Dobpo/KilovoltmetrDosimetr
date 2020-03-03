package com.idobro.kilovoltmetr_dosimetr.utils;

import androidx.annotation.Nullable;

public final class StringUtils {
    public static final String DEFAULT_PLACEHOLDER = "-";

    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.trim().isEmpty();
    }
}
