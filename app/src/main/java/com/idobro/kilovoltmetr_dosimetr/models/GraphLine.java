package com.idobro.kilovoltmetr_dosimetr.models;

import androidx.annotation.StringDef;

import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_FIRST_CHANEL;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_SECOND_CHANEL;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_SECOND_TO_FIRST;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_THIRD_CHANEL;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_THIRD_OT_FIRST;
import static com.idobro.kilovoltmetr_dosimetr.models.GraphLine.FRONT_THIRD_TO_SECOND;

@StringDef({FRONT_FIRST_CHANEL,
        FRONT_SECOND_CHANEL,
        FRONT_THIRD_CHANEL,
        FRONT_THIRD_OT_FIRST,
        FRONT_THIRD_TO_SECOND,
        FRONT_SECOND_TO_FIRST})
public @interface GraphLine {
    String FRONT_FIRST_CHANEL = "FRONT_FIRST_CHANEL";
    String FRONT_SECOND_CHANEL = "FRONT_SECOND_CHANEL";
    String FRONT_THIRD_CHANEL = "FRONT_THIRD_CHANEL";

    String FRONT_THIRD_OT_FIRST = "FRONT_THIRD_OT_FIRST";
    String FRONT_THIRD_TO_SECOND = "FRONT_THIRD_TO_SECOND";
    String FRONT_SECOND_TO_FIRST = "FRONT_SECOND_TO_FIRST";
}