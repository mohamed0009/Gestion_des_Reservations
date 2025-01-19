package com.gui.utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconUtils {
    public static final String ICON_PATH = "/resources/icons/";

    // Application icons
    public static final ImageIcon APP_ICON = loadIcon("app_icon.png");
    public static final ImageIcon CLIENT_ICON = loadIcon("client.png");
    public static final ImageIcon ROOM_ICON = loadIcon("room.png");
    public static final ImageIcon RESERVATION_ICON = loadIcon("reservation.png");
    public static final ImageIcon CATEGORY_ICON = loadIcon("category.png");
    public static final ImageIcon DASHBOARD_ICON = loadIcon("dashboard.png");
    public static final ImageIcon STATS_ICON = loadIcon("stats.png");

    private static ImageIcon loadIcon(String filename) {
        URL resource = IconUtils.class.getResource(ICON_PATH + filename);
        if (resource != null) {
            return new ImageIcon(resource);
        } else {
            System.err.println("Could not load icon: " + filename);
            return null;
        }
    }

    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon == null)
            return null;
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    public static void setFrameIcon(JFrame frame, ImageIcon icon) {
        if (icon != null) {
            frame.setIconImage(icon.getImage());
        }
    }

    public static void setInternalFrameIcon(JInternalFrame frame, ImageIcon icon) {
        if (icon != null) {
            frame.setFrameIcon(icon);
        }
    }
}