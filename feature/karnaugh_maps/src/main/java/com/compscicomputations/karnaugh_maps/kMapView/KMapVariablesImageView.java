package com.compscicomputations.karnaugh_maps.kMapView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.NotificationManagerCompat;

import com.compscicomputations.karnaugh_maps.Config;
import com.compscicomputations.karnaugh_maps.utils.ListOfMinterms;
import com.compscicomputations.karnaugh_maps.utils.MinTerm;

import java.util.ArrayList;

public class KMapVariablesImageView extends AppCompatImageView {
    final Alignment alignment = new Alignment();
    public int[] allMinTerms;
    int imageResource = -1;
    int imageResourceMapInverted = -1;
    private int imageWidthSize = 0;
    ListOfMinterms listOfMinTermsToDraw;
    Boolean mapInverted = false;
    private final ArrayList<Integer> minTermIntegersMapLeft = new ArrayList<>();
    private final ArrayList<Integer> minTermIntegersMapRight = new ArrayList<>();
    String minTermNotToGroup = "0";
    String minTermToGroup = "1";
    String[] minTermsString;
    int mintermDrawAnimation = 0;
    public final int[] noMinterms = new int[0];
    OnKmapAnimationListener onKmapAnimationListener;
    private final Paint paint = new Paint();
    //private Boolean sop = true;

    public interface OnKmapAnimationListener {
        void onAnimate();

        void onTick(int i);

        void stopAnimate();
    }

    public KMapVariablesImageView(Context context) {
        super(context);
    }

    public KMapVariablesImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public KMapVariablesImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private static Bitmap bitmapFromResource(Resources resources, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, i, options), i2, (options.outHeight * i2) / options.outWidth, true);
    }

    private void setImageResource() {
        if (this.imageResource <= 0) {
            return;
        }
        if (this.mapInverted) {
            setImageBitmap(bitmapFromResource(getResources(), this.imageResourceMapInverted, this.imageWidthSize));
        } else {
            setImageBitmap(bitmapFromResource(getResources(), this.imageResource, this.imageWidthSize));
        }
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.imageWidthSize = MeasureSpec.getSize(i);
        setImageResource();
    }

    public void checkInversionBtn(double d, double d2) {
        boolean z = false;
        boolean z2 = d < ((double) this.alignment.invertBtnposition);
        if (d2 < ((double) this.alignment.invertBtnposition)) {
            z = true;
        }
        if (z2 && z) {
            this.mapInverted = !this.mapInverted;
            setImageResource();
            invalidate();
        }
    }

    public int coordinatesToMinterm(int i, int i2) {
        switch ((i2 * 4) + i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 2;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 7;
            case 7:
                return 6;
            case 8:
                return 12;
            case 9:
                return 13;
            case 10:
                return 15;
            case 11:
                return 14;
            case 12:
                return 8;
            case 13:
                return 9;
            case 14:
                return 11;
            case 15:
                return 10;
            default:
                Log.e("KMap", "Error: x:" + i + " y:" + i2);
                return -1;
        }
    }

    public int mintermToCoordinatesX(int i) {
        switch (i) {
            case 0:
            case 4:
            case 8:
            case 12:
                return 0;
            case 1:
            case 5:
            case 9:
            case 13:
                return 1;
            case 2:
            case 6:
            case 10:
            case 14:
                return 3;
            case 3:
            case 7:
            case 11:
            case 15:
                return 2;
            default:
                Log.e("KMap", "Error:" + i);
                return -1;
        }
    }

    public int minTermToCoordinatesY(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
                return 0;
            case 4:
            case 5:
            case 6:
            case 7:
                return 1;
            case 8:
            case 9:
            case 10:
            case 11:
                return 3;
            case 12:
            case 13:
            case 14:
            case 15:
                return 2;
            default:
                Log.e("KMap", "Error:" + i);
                return -1;
        }
    }

    private void drawLineReferenceScreen(Canvas canvas, Paint paint2, float f, float f2, float f3, float f4) {
        canvas.drawLine(f * ((float) canvas.getWidth()), f2 * ((float) canvas.getHeight()), f3 * ((float) canvas.getWidth()), f4 * ((float) canvas.getHeight()), paint2);
    }

    private void drawLines(Canvas canvas, Paint paint2, int[] iArr) {
        boolean bool;
        boolean bool2 = false;
        int i = iArr[0];
        boolean bool3 = true;
        int i2 = iArr[1];
        int i3 = iArr[2];
        int i4 = iArr[3];
        float f = this.alignment.rectPosX[i] + (this.alignment.rectWidth / 2.0f);
        float f2 = this.alignment.rectPosY[i];
        float f3 = this.alignment.rectPosX[i] + this.alignment.rectWidth;
        float f4 = this.alignment.rectPosY[i] + (this.alignment.rectHeight / 2.0f);
        float f5 = this.alignment.rectPosX[i] - (this.alignment.rectWidth * 0.25f);
        float f6 = this.alignment.rectPosY[i];
        float f7 = this.alignment.rectPosX[i] + this.alignment.rectWidth;
        float f8 = this.alignment.rectPosY[i] + (this.alignment.rectHeight * 1.25f);
        float f9 = this.alignment.rectPosX[i2] + this.alignment.rectWidth;
        float f10 = this.alignment.rectPosY[i2] + (this.alignment.rectHeight / 2.0f);
        float f11 = this.alignment.rectPosX[i2] + (this.alignment.rectWidth / 2.0f);
        float f12 = this.alignment.rectPosY[i2] + this.alignment.rectHeight;
        float f13 = this.alignment.rectPosX[i2] + this.alignment.rectWidth;
        float f14 = this.alignment.rectPosY[i2] - (this.alignment.rectHeight * 0.25f);
        float f15 = this.alignment.rectPosX[i2] - (this.alignment.rectWidth * 0.25f);
        float f16 = this.alignment.rectPosY[i2] + this.alignment.rectHeight;
        float f17 = this.alignment.rectPosX[i3] + (this.alignment.rectWidth / 2.0f);
        float f18 = this.alignment.rectPosY[i3] + this.alignment.rectHeight;
        float f19 = this.alignment.rectPosX[i3];
        float f20 = this.alignment.rectPosY[i3] + (this.alignment.rectHeight / 2.0f);
        float f21 = this.alignment.rectPosX[i3] + (this.alignment.rectWidth * 1.25f);
        float f22 = this.alignment.rectPosY[i3] + this.alignment.rectHeight;
        float f23 = this.alignment.rectPosX[i3];
        float f24 = this.alignment.rectPosY[i3] - (this.alignment.rectHeight * 0.25f);
        float f25 = this.alignment.rectPosX[i4];
        float f26 = this.alignment.rectPosY[i4] + (this.alignment.rectHeight / 2.0f);
        float f27 = this.alignment.rectPosX[i4] + (this.alignment.rectWidth / 2.0f);
        float f28 = this.alignment.rectPosY[i4];
        float f29 = this.alignment.rectPosX[i4];
        float f30 = this.alignment.rectPosY[i4] + (this.alignment.rectHeight * 1.25f);
        float f31 = this.alignment.rectPosX[i4] + (this.alignment.rectWidth * 1.25f);
        float f32 = this.alignment.rectPosY[i4];
        if (f10 > f4) {
            drawLineReferenceScreen(canvas, paint2, f3, f4, f9, f10);
        }
        if (f11 > f17) {
            drawLineReferenceScreen(canvas, paint2, f11, f12, f17, f18);
        }
        if (f26 < f20) {
            drawLineReferenceScreen(canvas, paint2, f19, f20, f25, f26);
        }
        if (f27 < f) {
            drawLineReferenceScreen(canvas, paint2, f27, f28, f, f2);
        }
        if (f10 < f4) {
            bool = false;
            if (f11 < f17) {
                bool2 = true;
                bool3 = false;
            }
        } else if (f27 > f) {
            bool = true;
            bool3 = false;
        } else {
            bool3 = false;
            bool = false;
        }
        if (bool2) {
            drawLineReferenceScreen(canvas, paint2, f5, f6, f, f2);
            drawLineReferenceScreen(canvas, paint2, f15, f16, f11, f12);
            drawLineReferenceScreen(canvas, paint2, f13, f14, f9, f10);
            drawLineReferenceScreen(canvas, paint2, f23, f24, f19, f20);
            drawLineReferenceScreen(canvas, paint2, f21, f22, f17, f18);
            drawLineReferenceScreen(canvas, paint2, f31, f32, f27, f28);
            drawLineReferenceScreen(canvas, paint2, f29, f30, f25, f26);
            drawLineReferenceScreen(canvas, paint2, f7, f8, f3, f4);
        }
        if (bool3) {
            drawLineReferenceScreen(canvas, paint2, f3, f4, f7, f8);
            drawLineReferenceScreen(canvas, paint2, f13, f14, f9, f10);
            drawLineReferenceScreen(canvas, paint2, f19, f20, f23, f24);
            drawLineReferenceScreen(canvas, paint2, f29, f30, f25, f26);
        }
        if (bool) {
            drawLineReferenceScreen(canvas, paint2, f5, f6, f, f2);
            drawLineReferenceScreen(canvas, paint2, f15, f16, f11, f12);
            drawLineReferenceScreen(canvas, paint2, f17, f18, f21, f22);
            drawLineReferenceScreen(canvas, paint2, f31, f32, f27, f28);
        }
    }

    private int[] findCorners(ArrayList<Integer> arrayList) {
        boolean z;
        boolean z2;
        int i = NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
        int i2 = NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
        int i3 = 1000;
        int i4 = 1000;
        for (int i5 = 0; i5 < arrayList.size(); i5++) {
            int mintermToCoordinatesX = mintermToCoordinatesX(arrayList.get(i5));
            int mintermToCoordinatesY = minTermToCoordinatesY(arrayList.get(i5));
            if (mintermToCoordinatesX < i4) {
                i4 = mintermToCoordinatesX;
            }
            if (mintermToCoordinatesY < i3) {
                i3 = mintermToCoordinatesY;
            }
            if (mintermToCoordinatesX > i) {
                i = mintermToCoordinatesX;
            }
            if (mintermToCoordinatesY > i2) {
                i2 = mintermToCoordinatesY;
            }
        }
        int coordinatesToMinterm = coordinatesToMinterm(i, i3);
        int coordinatesToMinterm2 = coordinatesToMinterm(i, i2);
        int coordinatesToMinterm3 = coordinatesToMinterm(i4, i2);
        int coordinatesToMinterm4 = coordinatesToMinterm(i4, i3);
        int i6 = i4;
        while (true) {
            if (i6 >= i) {
                z = false;
                break;
            } else if (!arrayList.contains(coordinatesToMinterm(i6, i3))) {
                z = true;
                break;
            } else {
                i6++;
            }
        }
        while (true) {
            if (i3 >= i2) {
                z2 = false;
                break;
            } else if (!arrayList.contains(coordinatesToMinterm(i4, i3))) {
                z2 = true;
                break;
            } else {
                i3++;
            }
        }
        if (!z) {
            int i7 = coordinatesToMinterm4;
            coordinatesToMinterm4 = coordinatesToMinterm;
            coordinatesToMinterm = i7;
            int i8 = coordinatesToMinterm3;
            coordinatesToMinterm3 = coordinatesToMinterm2;
            coordinatesToMinterm2 = i8;
        }
        if (z2) {
            int i9 = coordinatesToMinterm4;
            coordinatesToMinterm4 = coordinatesToMinterm3;
            coordinatesToMinterm3 = i9;
        } else {
            int i10 = coordinatesToMinterm2;
            coordinatesToMinterm2 = coordinatesToMinterm;
            coordinatesToMinterm = i10;
        }
        return new int[]{coordinatesToMinterm4, coordinatesToMinterm3, coordinatesToMinterm, coordinatesToMinterm2};
    }

    public void setMinterms(int i) {
        String str = this.minTermsString[i];
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 88:
                if (str.equals("X")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.minTermsString[i] = "1";
                return;
            case 1:
                this.minTermsString[i] = "X";
                return;
            case 2:
                this.minTermsString[i] = "0";
                return;
            default:
        }
    }

    public int[] getMinterms() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            String[] strArr = this.minTermsString;
            if (i2 >= strArr.length) {
                break;
            }
            if (strArr[i2].equals(this.minTermToGroup)) {
                i3++;
            }
            i2++;
        }
        int[] iArr = new int[i3];
        int i4 = 0;
        while (true) {
            String[] strArr2 = this.minTermsString;
            if (i >= strArr2.length) {
                return iArr;
            }
            if (strArr2[i].equals(this.minTermToGroup)) {
                iArr[i4] = i;
                i4++;
            }
            i++;
        }
    }

    public int[] getDontCares() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            String[] strArr = this.minTermsString;
            if (i2 >= strArr.length) {
                break;
            }
            if (strArr[i2].equals("X")) {
                i3++;
            }
            i2++;
        }
        int[] iArr = new int[i3];
        int i4 = 0;
        while (true) {
            String[] strArr2 = this.minTermsString;
            if (i >= strArr2.length) {
                return iArr;
            }
            if (strArr2[i].equals("X")) {
                iArr[i4] = i;
                i4++;
            }
            i++;
        }
    }

    public void setMinTerms(int[] iArr, int[] iArr2) {
        int i = 0;
        while (true) {
            String[] strArr = this.minTermsString;
            if (i >= strArr.length) {
                break;
            }
            strArr[i] = "0";
            i++;
        }
        for (int i2 : iArr) {
            this.minTermsString[i2] = "1";
        }
        for (int i3 : iArr2) {
            this.minTermsString[i3] = "X";
        }
    }

    public int findClosestMinterm(double d, double d2) {
        boolean z = true;
        int i = -1;
        if ((d < ((double) this.alignment.minPositionInsideKMapX)) || (d2 < ((double) this.alignment.minPositionInsideKMapY))) {
            return -1;
        }
        boolean z2 = d > ((double) this.alignment.maxPositionInsideKMapX);
        if (d2 <= ((double) this.alignment.maxPositionInsideKMapY)) {
            z = false;
        }
        if (z2 || z) {
            return -1;
        }
        double d3 = 1000.0d;
        for (int i2 = 0; i2 < this.alignment.posX.length; i2++) {
            double posX = this.alignment.posX[i2];
            double abs = Math.abs(posX - d);
            double posY = this.alignment.posY[i2];
            double abs2 = abs + Math.abs(posY - d2);
            if (abs2 < d3) {
                i = this.mapInverted ? this.alignment.inversionConversion[i2] : i2;
                d3 = abs2;
            }
        }
        return i;
    }

    private void drawCircleArc(Canvas canvas, Paint paint2, int i, int i2) {
        float width = this.alignment.rectPosX[i] * ((float) canvas.getWidth());
        float height = this.alignment.rectPosY[i] * ((float) canvas.getHeight());
        RectF rectF = new RectF(width, height, (this.alignment.rectWidth * ((float) canvas.getWidth())) + width, (this.alignment.rectHeight * ((float) canvas.getHeight())) + height);
        if (i2 == 1) {
            canvas.drawArc(rectF, -90.0f, 90.0f, false, paint2);
        }
        if (i2 == 2) {
            canvas.drawArc(rectF, 0.0f, 90.0f, false, paint2);
        }
        if (i2 == 3) {
            canvas.drawArc(rectF, 180.0f, -90.0f, false, paint2);
        }
        if (i2 == 4) {
            canvas.drawArc(rectF, 270.0f, -90.0f, false, paint2);
        }
    }

    public int getCenterX() {
        return (int) (getX() + (((float) getWidth()) * this.alignment.centerX));
    }

    public int getCenterY() {
        return (int) (getY() + (((float) getHeight()) * this.alignment.centerY));
    }

    public void setOnKmapAnimationListener(OnKmapAnimationListener onKmapAnimationListener2) {
        this.onKmapAnimationListener = onKmapAnimationListener2;
    }

    public void setDrawGroups(ListOfMinterms listOfMinterms) {
        this.listOfMinTermsToDraw = listOfMinterms;
        if (Config.animation == 0) {
            this.mintermDrawAnimation = this.listOfMinTermsToDraw.size();
            invalidate();
            return;
        }
        OnKmapAnimationListener onKmapAnimationListener2 = this.onKmapAnimationListener;
        if (onKmapAnimationListener2 != null) {
            onKmapAnimationListener2.onAnimate();
        }
        this.mintermDrawAnimation = 0;
        ValueAnimator ofInt = ValueAnimator.ofInt(0, this.listOfMinTermsToDraw.size());
        ofInt.setDuration((long) Config.animation * 1000 * this.listOfMinTermsToDraw.size());
        ofInt.setInterpolator(new LinearInterpolator());
        ofInt.addUpdateListener(valueAnimator -> {
            int intValue = (Integer) valueAnimator.getAnimatedValue();
            if (intValue != KMapVariablesImageView.this.mintermDrawAnimation) {
                KMapVariablesImageView.this.mintermDrawAnimation = intValue;
                if (KMapVariablesImageView.this.onKmapAnimationListener != null) {
                    KMapVariablesImageView.this.onKmapAnimationListener.onTick(intValue);
                }
                KMapVariablesImageView.this.invalidate();
            }
        });
        ofInt.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (KMapVariablesImageView.this.onKmapAnimationListener != null) {
                    KMapVariablesImageView.this.onKmapAnimationListener.stopAnimate();
                }
            }
        });
        ofInt.start();
    }

    private void drawAllOnesAndZeros(Canvas canvas, Paint paint2) {
        int i = 0;
        while (true) {
            String[] strArr = this.minTermsString;
            if (i < strArr.length) {
                String str = strArr[i];
                if (!str.equals(this.minTermNotToGroup)) {
                    canvas.save();
                    canvas.rotate(this.alignment.mapAngle, this.alignment.posX[i] * ((float) canvas.getWidth()), this.alignment.posY[i] * ((float) canvas.getHeight()));
                    if (this.mapInverted) {
                        canvas.drawText(str, this.alignment.posX[this.alignment.drawInversion[i]] * ((float) canvas.getWidth()), this.alignment.posY[this.alignment.drawInversion[i]] * ((float) canvas.getHeight()), paint2);
                    } else {
                        canvas.drawText(str, this.alignment.posX[i] * ((float) canvas.getWidth()), this.alignment.posY[i] * ((float) canvas.getHeight()), paint2);
                    }
                    canvas.restore();
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.reset();
//        this.paint.setColor(View.MEASURED_STATE_MASK);
        this.paint.setColor(Color.GRAY);
        this.paint.setTextSize((this.alignment.rectWidth / 2.0f) * ((float) getWidth()));
        drawAllOnesAndZeros(canvas, this.paint);
        int i = 0;
        for (int i2 = 0; i2 < this.mintermDrawAnimation; i2++) {
            MinTerm minterm = this.listOfMinTermsToDraw.getMinterm(i2);
            if (i == Config.minTermColors.length) {
                i = 0;
            }
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth((this.alignment.rectWidth * ((float) getWidth())) / 10.0f);
            this.paint.setColor(Config.minTermColors[i]);
            this.paint.setAlpha(140);
            this.minTermIntegersMapRight.clear();
            this.minTermIntegersMapLeft.clear();
            for (int i3 = 0; i3 < minterm.minTermIntegers.size(); i3++) {
                int intValue = minterm.minTermIntegers.get(i3);
                if (this.mapInverted) {
                    intValue = this.alignment.drawInversion[intValue];
                }
                if (intValue < 16) {
                    this.minTermIntegersMapLeft.add(intValue);
                } else {
                    this.minTermIntegersMapRight.add(intValue);
                }
            }
            if (!this.minTermIntegersMapLeft.isEmpty()) {
                int[] findCorners = findCorners(this.minTermIntegersMapLeft);
                drawCircleArc(canvas, this.paint, findCorners[0], 1);
                drawCircleArc(canvas, this.paint, findCorners[1], 2);
                drawCircleArc(canvas, this.paint, findCorners[2], 3);
                drawCircleArc(canvas, this.paint, findCorners[3], 4);
                drawLines(canvas, this.paint, findCorners);
            }
            if (!this.minTermIntegersMapRight.isEmpty()) {
                int[] findCorners2 = findCorners(this.minTermIntegersMapRight);
                drawCircleArc(canvas, this.paint, findCorners2[0], 1);
                drawCircleArc(canvas, this.paint, findCorners2[1], 2);
                drawCircleArc(canvas, this.paint, findCorners2[2], 3);
                drawCircleArc(canvas, this.paint, findCorners2[3], 4);
                drawLines(canvas, this.paint, findCorners2);
            }
            i++;
        }
    }
}
