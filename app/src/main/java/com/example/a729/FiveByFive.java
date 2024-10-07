package com.example.a729;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.time.LocalTime;
import java.util.ArrayList;

public class FiveByFive extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_five_by_five);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            gestureDetector = new GestureDetector(this, this);
            return insets;
        });
        final float width = getResources().getDisplayMetrics().widthPixels * 0.187f;
        Button[][] tiles = {
                {findViewById(R.id.tile00), findViewById(R.id.tile01), findViewById(R.id.tile02), findViewById(R.id.tile03), findViewById(R.id.tile04)},
                {findViewById(R.id.tile10), findViewById(R.id.tile11), findViewById(R.id.tile12), findViewById(R.id.tile13), findViewById(R.id.tile14)},
                {findViewById(R.id.tile20), findViewById(R.id.tile21), findViewById(R.id.tile22), findViewById(R.id.tile23), findViewById(R.id.tile24)},
                {findViewById(R.id.tile30), findViewById(R.id.tile31), findViewById(R.id.tile32), findViewById(R.id.tile33), findViewById(R.id.tile34)},
                {findViewById(R.id.tile40), findViewById(R.id.tile41), findViewById(R.id.tile42), findViewById(R.id.tile43), findViewById(R.id.tile44)}
        };
        int index = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            index = LocalTime.now().getSecond() % 25;
        int i = index / 5, j = index % 5;
        for (int t1 = 0; t1 < 5; t1++)
            for (int t2 = 0; t2 < 5; t2++) {
                tiles[t1][t2].setX(width * t2);
                tiles[t1][t2].setY(width * t1);
                tiles[t1][t2].setVisibility(View.GONE);
            }
        tiles[i][j].setVisibility(View.VISIBLE);
        Button unchoose = findViewById(R.id.unchoose);
        unchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

    }

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null)
            return false;
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0)
                    onSwipeDown();
                else
                    onSwipeUp();
            }
        }
        return true;
    }

    void onSwipeUp() {
        Tiles25 t = new Tiles25();
        t.init(this);
        int h;
        String num;
        boolean add = false;
        for (int j = 0; j < 5; j++) {
            for (int i = 1; i < 5; i++) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = i - 1;
                    while (h >= 0 && t.tilesOnBoard[h][j] == null) {
                        h--;
                    }
                    h++;
                    if (t.tilesOnBoard[h][j] == null) {
                        t.tilesOnBoard[h][j] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 3; i++) {
                if (t.tilesOnBoard[i][j] != null && t.tilesOnBoard[i + 1][j] != null && t.tilesOnBoard[i + 2][j] != null) {
                    num = (String) t.tilesOnBoard[i][j].getText();
                    if (t.tilesOnBoard[i + 1][j].getText() == num && t.tilesOnBoard[i + 2][j].getText() == num) {
                        add = true;
                        t.tilesOnBoard[i + 1][j].setVisibility(View.GONE);
                        t.tilesOnBoard[i + 2][j].setVisibility(View.GONE);
                        t.tilesOnBoard[i + 1][j] = null;
                        t.tilesOnBoard[i + 2][j] = null;
                        switch (num) {
                            case "3":
                                t.tilesOnBoard[i][j].setText(R.string.s9);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c9)));
                                break;
                            case "9":
                                t.tilesOnBoard[i][j].setText(R.string.s27);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c27)));
                                break;
                            case "27":
                                t.tilesOnBoard[i][j].setText(R.string.s81);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c81)));
                                break;
                            case "81":
                                t.tilesOnBoard[i][j].setText(R.string.s243);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c243)));
                                break;
                            case "243":
                                t.tilesOnBoard[i][j].setText(R.string.s729);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c729)));
                                break;
                            case "729":
                                t.tilesOnBoard[i][j].setText(R.string.s2187);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c2187)));
                                break;
                            case "2187":
                                t.tilesOnBoard[i][j].setText(R.string.s6561);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c6561)));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 1; i < 5; i++) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = i - 1;
                    while (h >= 0 && t.tilesOnBoard[h][j] == null) {
                        h--;
                    }
                    h++;
                    if (t.tilesOnBoard[h][j] == null) {
                        t.tilesOnBoard[h][j] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        moveTiles(t.tilesOnBoard, t.width, t.tiles, add);
    }

    void onSwipeLeft() {
        Tiles25 t = new Tiles25();
        t.init(this);
        int h;
        String num;
        boolean add = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = j - 1;
                    while (h >= 0 && t.tilesOnBoard[i][h] == null) {
                        h--;
                    }
                    h++;
                    if (t.tilesOnBoard[i][h] == null) {
                        t.tilesOnBoard[i][h] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (t.tilesOnBoard[i][j] != null && t.tilesOnBoard[i][j + 1] != null && t.tilesOnBoard[i][j + 2] != null) {
                    num = (String) t.tilesOnBoard[i][j].getText();
                    if (t.tilesOnBoard[i][j + 1].getText() == num && t.tilesOnBoard[i][j + 2].getText() == num) {
                        add = true;
                        t.tilesOnBoard[i][j + 1].setVisibility(View.GONE);
                        t.tilesOnBoard[i][j + 2].setVisibility(View.GONE);
                        t.tilesOnBoard[i][j + 1] = null;
                        t.tilesOnBoard[i][j + 2] = null;
                        switch (num) {
                            case "3":
                                t.tilesOnBoard[i][j].setText(R.string.s9);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c9)));
                                break;
                            case "9":
                                t.tilesOnBoard[i][j].setText(R.string.s27);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c27)));
                                break;
                            case "27":
                                t.tilesOnBoard[i][j].setText(R.string.s81);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c81)));
                                break;
                            case "81":
                                t.tilesOnBoard[i][j].setText(R.string.s243);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c243)));
                                break;
                            case "243":
                                t.tilesOnBoard[i][j].setText(R.string.s729);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c729)));
                                break;
                            case "729":
                                t.tilesOnBoard[i][j].setText(R.string.s2187);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c2187)));
                                break;
                            case "2187":
                                t.tilesOnBoard[i][j].setText(R.string.s6561);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c6561)));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = j - 1;
                    while (h >= 0 && t.tilesOnBoard[i][h] == null) {
                        h--;
                    }
                    h++;
                    if (t.tilesOnBoard[i][h] == null) {
                        t.tilesOnBoard[i][h] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        moveTiles(t.tilesOnBoard, t.width, t.tiles, add);
    }

    void onSwipeRight() {
        Tiles25 t = new Tiles25();
        t.init(this);
        int h;
        String num;
        boolean add = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 3; j >= 0; j--) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = j + 1;
                    while (h < 5 && t.tilesOnBoard[i][h] == null) {
                        h++;
                    }
                    h--;
                    if (t.tilesOnBoard[i][h] == null) {
                        t.tilesOnBoard[i][h] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 4; j > 1; j--) {
                if (t.tilesOnBoard[i][j] != null && t.tilesOnBoard[i][j - 1] != null && t.tilesOnBoard[i][j - 2] != null) {
                    num = (String) t.tilesOnBoard[i][j].getText();
                    if (t.tilesOnBoard[i][j - 1].getText() == num && t.tilesOnBoard[i][j - 2].getText() == num) {
                        add = true;
                        t.tilesOnBoard[i][j - 1].setVisibility(View.GONE);
                        t.tilesOnBoard[i][j - 2].setVisibility(View.GONE);
                        t.tilesOnBoard[i][j - 1] = null;
                        t.tilesOnBoard[i][j - 2] = null;
                        switch (num) {
                            case "3":
                                t.tilesOnBoard[i][j].setText(R.string.s9);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c9)));
                                break;
                            case "9":
                                t.tilesOnBoard[i][j].setText(R.string.s27);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c27)));
                                break;
                            case "27":
                                t.tilesOnBoard[i][j].setText(R.string.s81);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c81)));
                                break;
                            case "81":
                                t.tilesOnBoard[i][j].setText(R.string.s243);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c243)));
                                break;
                            case "243":
                                t.tilesOnBoard[i][j].setText(R.string.s729);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c729)));
                                break;
                            case "729":
                                t.tilesOnBoard[i][j].setText(R.string.s2187);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c2187)));
                                break;
                            case "2187":
                                t.tilesOnBoard[i][j].setText(R.string.s6561);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c6561)));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 3; j >= 0; j--) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = j + 1;
                    while (h < 5 && t.tilesOnBoard[i][h] == null) {
                        h++;
                    }
                    h--;
                    if (t.tilesOnBoard[i][h] == null) {
                        t.tilesOnBoard[i][h] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        moveTiles(t.tilesOnBoard, t.width, t.tiles, add);
    }

    void onSwipeDown() {
        Tiles25 t = new Tiles25();
        t.init(this);
        int h;
        String num;
        boolean add = false;
        for (int j = 0; j < 5; j++) {
            for (int i = 3; i >= 0; i--) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = i + 1;
                    while (h < 5 && t.tilesOnBoard[h][j] == null) {
                        h++;
                    }
                    h--;
                    if (t.tilesOnBoard[h][j] == null) {
                        t.tilesOnBoard[h][j] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 4; i > 1; i--) {
                if (t.tilesOnBoard[i][j] != null && t.tilesOnBoard[i - 1][j] != null && t.tilesOnBoard[i - 2][j] != null) {
                    num = (String) t.tilesOnBoard[i][j].getText();
                    if (t.tilesOnBoard[i - 1][j].getText() == num && t.tilesOnBoard[i - 2][j].getText() == num) {
                        add = true;
                        t.tilesOnBoard[i - 1][j].setVisibility(View.GONE);
                        t.tilesOnBoard[i - 2][j].setVisibility(View.GONE);
                        t.tilesOnBoard[i - 1][j] = null;
                        t.tilesOnBoard[i - 2][j] = null;
                        switch (num) {
                            case "3":
                                t.tilesOnBoard[i][j].setText(R.string.s9);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c9)));
                                break;
                            case "9":
                                t.tilesOnBoard[i][j].setText(R.string.s27);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c27)));
                                break;
                            case "27":
                                t.tilesOnBoard[i][j].setText(R.string.s81);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c81)));
                                break;
                            case "81":
                                t.tilesOnBoard[i][j].setText(R.string.s243);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c243)));
                                break;
                            case "243":
                                t.tilesOnBoard[i][j].setText(R.string.s729);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c729)));
                                break;
                            case "729":
                                t.tilesOnBoard[i][j].setText(R.string.s2187);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c2187)));
                                break;
                            case "2187":
                                t.tilesOnBoard[i][j].setText(R.string.s6561);
                                t.tilesOnBoard[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c6561)));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 3; i >= 0; i--) {
                if (t.tilesOnBoard[i][j] != null) {
                    h = i + 1;
                    while (h < 5 && t.tilesOnBoard[h][j] == null) {
                        h++;
                    }
                    h--;
                    if (t.tilesOnBoard[h][j] == null) {
                        t.tilesOnBoard[h][j] = t.tilesOnBoard[i][j];
                        t.tilesOnBoard[i][j] = null;
                        add = true;
                    }
                }
            }
        }
        moveTiles(t.tilesOnBoard, t.width, t.tiles, add);
    }

    void moveTiles(Button[][] tb, float width, Button[][] tiles, boolean add) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<Animator> animations = new ArrayList<>();
        int xd, yd;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tb[i][j] != null) {
                    xd = (int) Math.abs(tb[i][j].getTranslationX() / width - j);
                    yd = (int) Math.abs(tb[i][j].getTranslationY() / width - i);
                    if (xd > 0) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(tb[i][j], "translationX", width * j);
                        animator.setDuration(50 * xd);
                        animations.add(animator);
                    } else if (yd > 0) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(tb[i][j], "translationY", width * i);
                        animator.setDuration(50 * yd);
                        animations.add(animator);
                    }
                }
            }
        }
        if (!animations.isEmpty()) {
            animatorSet.playTogether(animations);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (tb[i][j] != null) {
                                tb[i][j].setTranslationX(width*j);
                                tb[i][j].setTranslationY(width*i);
                            }
                        }
                    }
                    if (add)
                        addTile(tiles, tb, width);
                }
            });
            animatorSet.start();
        }
        else if (add)
            addTile(tiles, tb, width);
    }

    void addTile(Button[][] tiles, Button[][] tb, float width) {
        ArrayList<Integer> vac = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tb[i][j] == null) {
                    vac.add(5 * i + j);
                }
            }
        }
        if (!vac.isEmpty()) {
            int index = vac.get(0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                index = vac.get(LocalTime.now().getSecond() % vac.size());
            int y = index / 5, x = index % 5;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (tiles[i][j].getVisibility() == View.GONE) {
                        tb[y][x] = tiles[i][j];
                        tiles[i][j].setText(R.string.s3);
                        tiles[i][j].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.c3)));
                        tiles[i][j].setTranslationX(width * x);
                        tiles[i][j].setTranslationY(width * y);
                        tiles[i][j].setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }
        }
    }
}

class Tiles25 extends AppCompatActivity {
    Button[][] tiles, tilesOnBoard;
    float width;

    public void init(Activity a) {
        width = a.getResources().getDisplayMetrics().widthPixels * 0.187f;
        tiles = new Button[][]{
                {a.findViewById(R.id.tile00), a.findViewById(R.id.tile01), a.findViewById(R.id.tile02), a.findViewById(R.id.tile03), a.findViewById(R.id.tile04)},
                {a.findViewById(R.id.tile10), a.findViewById(R.id.tile11), a.findViewById(R.id.tile12), a.findViewById(R.id.tile13), a.findViewById(R.id.tile14)},
                {a.findViewById(R.id.tile20), a.findViewById(R.id.tile21), a.findViewById(R.id.tile22), a.findViewById(R.id.tile23), a.findViewById(R.id.tile24)},
                {a.findViewById(R.id.tile30), a.findViewById(R.id.tile31), a.findViewById(R.id.tile32), a.findViewById(R.id.tile33), a.findViewById(R.id.tile34)},
                {a.findViewById(R.id.tile40), a.findViewById(R.id.tile41), a.findViewById(R.id.tile42), a.findViewById(R.id.tile43), a.findViewById(R.id.tile44)}
        };
        tilesOnBoard = new Button[][]{
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tiles[i][j].getVisibility() == View.VISIBLE) {
                    tilesOnBoard[(int) (tiles[i][j].getTranslationY() / width)][(int) (tiles[i][j].getTranslationX() / width)] = tiles[i][j];
                }
            }
        }
    }
}