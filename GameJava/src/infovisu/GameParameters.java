package infovisu;

import processing.core.PConstants;

abstract interface GameParameters {
    final static int WINDOW_WIDTH = 800;
    final static int WINDOW_HEIGHT = 600;

    final static int PLATE_X = 300;
    final static int PLATE_Y = 20;
    final static int PLATE_Z = 300;

    final static float MIN_ANGLE = -PConstants.PI / 3;
    final static float MAX_ANGLE = PConstants.PI / 3;
    final static float MIN_SENSI = 0.1f;
    final static float MAX_SENSI = 1.0f;

    final static float SPHERE_RADIUS = 20f;
    final static float MOVER_Z = -SPHERE_RADIUS - PLATE_Y / 2;
    final static float G = 1f;
    final static float MU = 0.1f;
    final static float REBOUND = 0.7f;

    final static float CYLINDER_RADIUS = 30f;
    final static float CYLINDER_HEIGHT = 40f;
    final static int CYLINDER_RESOLUTION = 40;
    final static int CYLINDER_Z = -PLATE_Y / 2;

    final static float MIN_X = -PLATE_X / 2;
    final static float MAX_X = PLATE_X / 2;
    final static float MIN_Y = -PLATE_Z / 2;
    final static float MAX_Y = PLATE_Z / 2;
    
    final static int SCORE_HEIGHT = 120;

    final static int BACKGROUND_COLOR = 0xFFB4FAFA;
    final static int PLATE_COLOR = 0xFF10AA10;
    final static int BALL_COLOR = 0xFFAA1010;
    final static int CYLINDER_COLOR = 0xFF1010AA;
    final int SCORE_BACKGND_COLOR = 0xFFEEEEAA;
}
