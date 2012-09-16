import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;




public class My35 {
	public static void main(String[] args) {
		System.out.println("Программа работы с мышью");
		MyFrame frame=new MyFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setVisible(true);
		frame.show();
	}
}

//создание окна
class MyFrame extends JFrame{
	public MyFrame(){
		setTitle("Окно программы");
		setSize(300,200);
		MyPanel panel=new MyPanel();
		Container pane=getContentPane();
		pane.add(panel);
	}
}
//работа с инструментами панели
class MyPanel extends JPanel{
	private ArrayList<Ellipse2D> circle;//массив координат окружностей
	private Ellipse2D current;	//массив текущего положения для разных нужд	
	public MyPanel(){
		circle=new ArrayList<Ellipse2D> ();
		current=null;
		addMouseListener(new MyMouse());//регистрация класса MyMouse как слушателя событий MyPanel
		addMouseMotionListener(new MyMove ());//регистрация класса MyMove как слушателя событий MyPanel
	}
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;//приведение типов
		for (int i=0; i<circle.size(); i++){//отрисовать закрашенные окружности из массива
			g2.setColor(new Color ((i*100+20)%255,(i*70+40)%255,(i*120+10)%255));//произвольные числа для генерации цвета
			g2.fill((Ellipse2D) circle.get(i));
		}
	}
	
	//создание и удаление объектов мышью
	private class MyMouse extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent event) {
			//проверка: не попадает курсор на окружность
			current = find(event.getPoint());
			if (current==null)add(event.getPoint());
			//super.mousePressed(event);
		}
		@Override
		public void mouseClicked(MouseEvent event) {//двойное нажатие 
			if (event.getClickCount()>=2){//для 2 кликов мышью
				current = find(event.getPoint());
				if (current!=null) remove (current);
			}
			//super.mouseClicked(event);
		}
	}
	
	//перетаскивание объектов мышью
	private class MyMove implements MouseMotionListener{
		public void mouseMoved(MouseEvent event){
			if(find(event.getPoint())==null)
				setCursor(Cursor.getDefaultCursor());
			else
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		public void mouseDragged(MouseEvent event){
			if (current!=null){
				current.setFrame(event.getX()-10, event.getY()-10,20,20);
				repaint ();
			}
		}
	}

	//добавить в массив окружность
	public void add(Point2D p) {
		current = new Ellipse2D.Double(p.getX()-10, p.getY()-10,20,20);
		circle.add(current);
		repaint();
	}

	//присутствие окружности в массиве
	public Ellipse2D find(Point2D p) {
		for (int i=0; i<circle.size(); i++){
			Ellipse2D e= (Ellipse2D) circle.get (i);
			if (e.contains(p)) return e;
		}
		return null;
	}
	
	//удалить окружность
	public void remove(Ellipse2D e) {
		if (e==null) return;
		if (e==current) current=null;
		circle.remove(e);
		repaint();
	}
}
