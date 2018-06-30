package com.han;

import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.VetoableChangeListener;
import java.io.UnsupportedEncodingException;

/**
 * Created by han on 2018/6/30.
 */
public class snapScreen extends Container {

    private JButton encodeButton;
    private JTextArea t1;
    private Cavas cavas;
    private JPanel j1;
    private JPanel j2;
    private JPanel j3;
    private JButton decodeButton;
    private JScrollPane jsp;
    private JButton captureButton;
    private JTextArea tmsg;
    private JButton cropButton;
    private int x0, x1;
    private int y0, y1;
    private boolean isClick = false;
    BufferedImage screenshot = null;

    public snapScreen() {
        encodeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                qRCodeCommon((Graphics2D) cavas.getGraphics(), t1.getText(), "png", 10);

            }
        });
        decodeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                // 拷贝屏幕到一个BufferedImage对象screenshot
                try {
                    t1.setText("");

                    QRCodeDecoder decoder = new QRCodeDecoder();
                    String content = new String(decoder.decode(new TwoDimensionCodeImage(cavas.getBufferedImage())), "utf-8");
                    t1.setText(content);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

            }
        });
        cavas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }
        });


        cavas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton()==MouseEvent.BUTTON1) {
                    if (!isClick) {
                        isClick = true;
                        x0 = e.getX();
                        y0 = e.getY();
                    } else {
                        isClick = false;
                        x1 = e.getX();
                        y1 = e.getY();
                        Graphics2D gs = (Graphics2D) cavas.getBufferedImage().getGraphics();
                        gs.setColor(Color.black);
                        gs.drawLine(x0, y0, x1, y0);
                        gs.drawLine(x0, y0, x0, y1);
                        gs.drawLine(x1, y0, x1, y1);
                        gs.drawLine(x0, y1, x1, y1);


                        tmsg.setText(tmsg.getText() + "(" + x0 + "," + y0 + "):(" + x1 + "," + y1 + ")\n");
                        System.out.printf("%d %d %d %d \n", x0, y0, x1, y1);

                    }

                }else {
                    isClick=false;
                    x0=0;
                    y0=0;

                }
            }
        });

        captureButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                try {
                    screenshot = (new Robot())
                            .createScreenCapture(new Rectangle(0, 0, d.width , d.height ));

                    cavas.setBufferedImage(screenshot);
                    cavas.paintImmediately(0, 0, d.width , d.height);
                } catch (AWTException e1) {
                    e1.printStackTrace();
                }


            }
        });

        cropButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                cavas.setBufferedImage(cavas.getBufferedImage().getSubimage(x0,y0,x1,y1));
                cavas.paintImmediately(x0,y0,x1-x0,y1-y0);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("snapScreen");
        frame.setContentPane(new snapScreen().j1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private BufferedImage qRCodeCommon(Graphics2D graphics, String content, String png, int size) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(size);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 图片尺寸
            int imgSize = 67 + 12 * (size - 1);
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);

            // 设置背景颜色
            graphics.setBackground(Color.WHITE);
            graphics.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            graphics.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 800) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            graphics.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }
            graphics.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;

    }


}


