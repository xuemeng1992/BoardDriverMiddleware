package com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.datatransfer;

import java.util.Observable;
import java.util.Observer;

public class DataObserver implements Observer {

    private IUpdataBox iUpdataBox;
    private IUpdataAssetCode iUpdataAssetCode;
    private IUpdataQConnBoard iUpdataQConnBoard;
    private IUpdataChipVersion iUpdataChipVersion;
    private IWriteAssetCode iWriteAssetCode;
    private ICommonDataObservable iCommonDataObservable;
    private ILampObservable iLampObservable;

    public void setiUpdataBox(IUpdataBox iUpdataBox) {
        this.iUpdataBox = iUpdataBox;
    }

    public void setiUpdataAssetCode(IUpdataAssetCode iUpdataAssetCode) {
        this.iUpdataAssetCode = iUpdataAssetCode;
    }

    public void setiUpdataQConnBoard(IUpdataQConnBoard iUpdataQConnBoard) {
        this.iUpdataQConnBoard = iUpdataQConnBoard;
    }

    public void setiUpdataChipVersion(IUpdataChipVersion iUpdataChipVersion) {
        this.iUpdataChipVersion = iUpdataChipVersion;
    }

    public void setiWriteAssetCode(IWriteAssetCode iWriteAssetCode) {
        this.iWriteAssetCode = iWriteAssetCode;
    }

    public void setiCommonDataObservable(ICommonDataObservable iCommonDataObservable) {
        this.iCommonDataObservable = iCommonDataObservable;
    }

    public void setiLampObservable(ILampObservable iLampObservable) {
        this.iLampObservable = iLampObservable;
    }

    public interface IUpdataBox {
        void updataBox(BoxDataObservable boxDataObservable);
    }

    public interface IUpdataAssetCode {
        void updataAssetCode(AssetCodeObservable assetCodeObservable);
    }

    public interface IUpdataQConnBoard {
        void updataQConnBoard(QConnBoardObservable qConnBoardObservable);
    }

    public interface IUpdataChipVersion {
        void updataChipVersion(ChipVersionObservable chipVersionObservable);
    }

    public interface IWriteAssetCode {
        void writeAssetCode(WriteAssetCodeObservable writeAssetCodeObservable);
    }

    public interface ICommonDataObservable {
        void commonDataObservable(CommonDataObservable commonDataObservable);
    }

    public interface ILampObservable {
        void lampObservable(LampObservable lampObservable);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BoxDataObservable) {
            BoxDataObservable boxDataObservable = (BoxDataObservable) o;
            if (iUpdataBox != null) {
                iUpdataBox.updataBox(boxDataObservable);
            }
        }
        if (o instanceof AssetCodeObservable) {
            AssetCodeObservable assetCodeObservable = (AssetCodeObservable) o;
            if (iUpdataAssetCode != null) {
                iUpdataAssetCode.updataAssetCode(assetCodeObservable);
            }
        }
        if (o instanceof QConnBoardObservable) {
            QConnBoardObservable qConnBoardObservable = (QConnBoardObservable) o;
            if (iUpdataQConnBoard != null) {
                iUpdataQConnBoard.updataQConnBoard(qConnBoardObservable);
            }
        }
        if (o instanceof ChipVersionObservable) {
            ChipVersionObservable chipVersionObservable = (ChipVersionObservable) o;
            if (iUpdataChipVersion != null) {
                iUpdataChipVersion.updataChipVersion(chipVersionObservable);
            }
        }
        if (o instanceof WriteAssetCodeObservable) {
            WriteAssetCodeObservable writeAssetCodeObservable = (WriteAssetCodeObservable) o;
            if (iWriteAssetCode != null) {
                iWriteAssetCode.writeAssetCode(writeAssetCodeObservable);
            }
        }
        if (o instanceof CommonDataObservable) {
            CommonDataObservable commonDataObservable = (CommonDataObservable) o;
            if (iCommonDataObservable != null) {
                iCommonDataObservable.commonDataObservable(commonDataObservable);
            }
        }
        if (o instanceof LampObservable) {
            LampObservable lampObservable = (LampObservable) o;
            if (iLampObservable != null) {
                iLampObservable.lampObservable(lampObservable);
            }
        }
    }
}
