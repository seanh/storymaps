package storymaps.ui;

/* ----------------------------------------------------------------------

    This file is part of ZoomDesk (c) Duncan Jauncey 2006

    ZoomDesk is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    ZoomDesk is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with ZoomDesk.  If not, see <http://www.gnu.org/licenses/>.

  ----------------------------------------------------------------------
*/

import java.awt.*;

public class Colours {
    public static Color BACKGROUND = Color.WHITE;
    public static Color FOREGROUND = Color.BLACK;

    public static Color BACKGROUND_POPUP = new Color(250,250,220);

    public static Color LAYER_TITLE = new Color(220,220,220);

    public static Color DOCUMENT_BORDER_ERROR = new Color(250,120,110);
    public static Color DOCUMENT_BORDER_SELECTED = new Color(100,125,205);
    public static Color DOCUMENT_BORDER = new Color(100,100,100);
    public static Color DOCUMENT_FAKE_LINES = new Color(230,230,235);

    public static Color DOCUMENT_PROGRESS_BAR_FOREGROUND = new Color(200,220,255);
    public static Color DOCUMENT_PROGRESS_BAR_BACKGROUND = new Color(240,245,255);

    public static Color DOCUMENT_CARET = new Color(200,150,210);
    public static Color DOCUMENT_SELECTION = new Color(255,255,215);


    public static Color COMMAND_BAR_COMMAND_TEXT = FOREGROUND;
    public static Color COMMAND_BAR_COMMAND_DESCRIPTION = new Color(100,120,160);


    public static Color BUTTON_BORDER = new Color(10,10,10);
    public static Color BUTTON_BORDER_LIGHT = new Color(50,50,50);

    public static Color RADIO_BUTTON_BACKGROUND = new Color(255, 220, 20);
    public static Color RADIO_BUTTON_BACKGROUND_SELECTED = RADIO_BUTTON_BACKGROUND.brighter();

    public static Color ERROR_TEXT = new Color(250,120,110);

    public static Color DARK_GREEN = new Color(100,150,100);

    public static Color BAR_BACKGROUND = new Color(230,230,230);

    public static Color lighter(Color c, int step) {
        int red = c.getRed() + step;
        int green = c.getGreen() + step;
        int blue = c.getBlue() + step;

        if( red > 255 ) {
            red = 255;
        }
        if( green > 255 ) {
            green = 255;
        }
        if( blue > 255 ) {
            blue = 255;
        }

        return new Color(red,green,blue);
    }

    public static Color greyOut(Color c) {
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();

        int average = (red + green + blue) / 3;
        average *= 1.3;

        red = average;
        green = average;
        blue = average;

        if( red > 255 ) {
            red = 255;
        }
        if( green > 255 ) {
            green = 255;
        }
        if( blue > 255 ) {
            blue = 255;
        }

        return new Color(red,green,blue);
    }
}