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

package storymaps.ui;

import java.awt.*;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;

public class Strokes {
    public static final Stroke DOCUMENT_BORDER = new PFixedWidthStroke(0.5f);
    public static final Stroke DOCUMENT_BORDER_SELECTED = new PFixedWidthStroke(2.0f);

    public static final Stroke DOCUMENT_FAKE_LINE_STROKE = new PFixedWidthStroke(4.0f);
//    public static final Stroke DOCUMENT_FAKE_LINE_STROKE = new BasicStroke(2.0f);

    public static final Stroke BASIC_FIXED_STROKE = new PFixedWidthStroke(0.5f);
    public static final Stroke BASIC_STROKE = new BasicStroke(1.0f);

    public static final Stroke THIN_FIXED_STROKE = new PFixedWidthStroke(0.1f);
    public static final Stroke THIN_STROKE = new BasicStroke(0.1f);

    public static final Stroke BIG_FAT_STROKE = new PFixedWidthStroke(2.0f);

}