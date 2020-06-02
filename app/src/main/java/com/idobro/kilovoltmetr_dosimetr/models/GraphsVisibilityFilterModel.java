package com.idobro.kilovoltmetr_dosimetr.models;

public class GraphsVisibilityFilterModel {
    private boolean frontFirstChanel;
    private boolean frontSecondChanel;
    private boolean frontThirdChanel;

    private boolean frontRelationThirdToFirst;
    private boolean frontRelationThirdToSecond;
    private boolean frontRelationSecondToFirst;

    private boolean fullFirstChanel;
    private boolean fullSecondChanel;
    private boolean fullThirdChanel;

    private boolean fullRelationThirdToFirst;
    private boolean fullRelationThirdToSecond;
    private boolean fullRelationSecondToFirst;

    public GraphsVisibilityFilterModel() {
        frontFirstChanel = false;
        frontSecondChanel = false;
        frontThirdChanel = false;
        frontRelationThirdToFirst = false;
        frontRelationThirdToSecond = false;
        frontRelationSecondToFirst = false;
        fullFirstChanel = false;
        fullSecondChanel = false;
        fullThirdChanel = false;
        fullRelationThirdToFirst = false;
        fullRelationThirdToSecond = false;
        fullRelationSecondToFirst = false;
    }

    public void setFrontFirstChanel(boolean frontFirstChanel) {
        this.frontFirstChanel = frontFirstChanel;
    }

    public void setFrontSecondChanel(boolean frontSecondChanel) {
        this.frontSecondChanel = frontSecondChanel;
    }

    public void setFrontThirdChanel(boolean frontThirdChanel) {
        this.frontThirdChanel = frontThirdChanel;
    }

    public void setFrontRelationThirdToFirst(boolean frontRelationThirdToFirst) {
        this.frontRelationThirdToFirst = frontRelationThirdToFirst;
    }

    public void setFrontRelationThirdToSecond(boolean frontRelationThirdToSecond) {
        this.frontRelationThirdToSecond = frontRelationThirdToSecond;
    }

    public void setFrontRelationSecondToFirst(boolean frontRelationSecondToFirst) {
        this.frontRelationSecondToFirst = frontRelationSecondToFirst;
    }

    public void setFullFirstChanel(boolean fullFirstChanel) {
        this.fullFirstChanel = fullFirstChanel;
    }

    public void setFullSecondChanel(boolean fullSecondChanel) {
        this.fullSecondChanel = fullSecondChanel;
    }

    public void setFullThirdChanel(boolean fullThirdChanel) {
        this.fullThirdChanel = fullThirdChanel;
    }

    public void setFullRelationThirdToFirst(boolean fullRelationThirdToFirst) {
        this.fullRelationThirdToFirst = fullRelationThirdToFirst;
    }

    public void setFullRelationThirdToSecond(boolean fullRelationThirdToSecond) {
        this.fullRelationThirdToSecond = fullRelationThirdToSecond;
    }

    public void setFullRelationSecondToFirst(boolean fullRelationSecondToFirst) {
        this.fullRelationSecondToFirst = fullRelationSecondToFirst;
    }

    public boolean isFrontFirstChanel() {
        return frontFirstChanel;
    }

    public boolean isFrontSecondChanel() {
        return frontSecondChanel;
    }

    public boolean isFrontThirdChanel() {
        return frontThirdChanel;
    }

    public boolean isFrontRelationThirdToFirst() {
        return frontRelationThirdToFirst;
    }

    public boolean isFrontRelationThirdToSecond() {
        return frontRelationThirdToSecond;
    }

    public boolean isFrontRelationSecondToFirst() {
        return frontRelationSecondToFirst;
    }

    public boolean isFullFirstChanel() {
        return fullFirstChanel;
    }

    public boolean isFullSecondChanel() {
        return fullSecondChanel;
    }

    public boolean isFullThirdChanel() {
        return fullThirdChanel;
    }

    public boolean isFullRelationThirdToFirst() {
        return fullRelationThirdToFirst;
    }

    public boolean isFullRelationThirdToSecond() {
        return fullRelationThirdToSecond;
    }

    public boolean isFullRelationSecondToFirst() {
        return fullRelationSecondToFirst;
    }
}
