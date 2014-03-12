package org.ong.android.apps.example.listviewpercent;

import org.ong.android.apps.example.listviewprcent.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 这个例子展示如何更新 Views。
 * 
 * @author izhaoad@gmail.com
 * @created 2013/07/17 18:18:38 GMT + 08:00 
 * @updated 2013/07/17 18:35:27 GMT + 08:00 
 */
public class MainActivity extends Activity implements View.OnClickListener {
	
	private ListView listView;
	private MyAdapter adapter;
	
	private View action;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		
		// Step 1:
		setContentView( R.layout.activity_main );
		
		// Step 2:
		findViews();
		
		// Step 3:
		setListeners();
		
		// Step 4: 载入数据... 
		load();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch ( v.getId() ) {
		case android.R.id.button1:
			
			computePercentAndUpdateUi();
		
			break;
		}
	}
	
	/**
	 * 
	 */
	private void findViews() {
		listView = (ListView) findViewById( android.R.id.list );
		action = findViewById( android.R.id.button1 );
	}
	
	/**
	 * 
	 */
	private void setListeners() {
		action.setOnClickListener( this );
	}
	
	/**
	 * 计算 Percent 并更新 UI。
	 */
	private void computePercentAndUpdateUi() {
		for ( int i = 0; i < listView.getCount(); i++ ) {
			final View child = listView.getChildAt( i );
			
			// 不可见的就忽略它
			if ( View.VISIBLE != child.getVisibility() ) continue; 
			
			final TextView primary = (TextView) child.findViewById( 
					R.id.prcent );
			final TextView secondary = (TextView) child.findViewById( 
					R.id.prcent_secondary );
			
			// 原始的宽度，由于我们的 Weight 都是 0.5 固此...
			final int maxWidth = primary.getWidth();
			
			// Parent 的宽度
//			final int maxWidth = child.getWidth();
			
			// Padding left + 百分比宽度 + padding right
			final int primaryWidthOfPercent = primary.getPaddingLeft() 
					+ (maxWidth / 100 * adapter.getItem( i ).intValue() ) 
					+ primary.getPaddingRight();
			final int secondaryWidthOfPercent = secondary.getPaddingLeft() 
					+ ( maxWidth / 100 * ( 1 + adapter.getItem( i ).intValue() ) ) 
					+ secondary.getPaddingRight();
			
			// 更新 Weight
			LinearLayout.LayoutParams pp = (LayoutParams) primary
					.getLayoutParams();
			LinearLayout.LayoutParams sp = (LayoutParams) secondary
					.getLayoutParams();
			
			// 重为 0 以致不会应用按比率分配原则
			pp.weight = sp.weight = 0;
			
			// 没应用 Delay 的操作
			//target.setWidth( widthOfprcent );
			
			// 为了让你看清过程，这里运用了 Delay 操作
			listView.postDelayed( new Runnable() {
				@Override
				public void run() {
					primary.setWidth( primaryWidthOfPercent );
					secondary.setWidth( secondaryWidthOfPercent );
				}
			}, 100 * i );
		}
	}
	
	/**
	 * 载入数据。
	 */
	private void load() {
		adapter = new MyAdapter( this );		
		listView.setAdapter( adapter );		
	}
	
	static class MyAdapter extends BaseAdapter {
		
		private Context context;
		
		private MyAdapter(Context context) {
			this.context = context;
		}
		
		static final Integer []DATA = new Integer[] {
			Integer.valueOf( 12 ), Integer.valueOf( 13 ),
			Integer.valueOf( 99 ), Integer.valueOf( 20 ),
			Integer.valueOf( 20 ), Integer.valueOf( 13 ),
			Integer.valueOf( 17 ), Integer.valueOf( 18 ),
			Integer.valueOf( 15 ), Integer.valueOf( 13 )
		};

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			// FIXME 这里仅实现简单的 View 组织，不作任何更新操作...
			if ( null == convertView ) {
				convertView = LayoutInflater.from( context )
						.inflate( R.layout.list_item, null );
				
				holder = new ViewHolder();
				
				holder.percent = (TextView) convertView.findViewById( 
						R.id.prcent );
				holder.secondary = (TextView) convertView.findViewById( 
						R.id.prcent_secondary );
				
				convertView.setTag( holder );
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// e.g. 12%
			String percent = String.format( "%s%%", 
					String.valueOf( getItem( position ) ) );
			
			String percentSecondary = String.format( "%s%%", 
					String.valueOf( 1 + getItem( position ).intValue() ) );
			
			holder.percent.setText( percent );
			holder.secondary.setText( percentSecondary );
			
			return convertView;
		}

		@Override
		public int getCount() {
			return DATA.length;
		}

		@Override
		public Integer getItem(int position) {
			return DATA[ position ];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private final class ViewHolder {
			private TextView percent;
			private TextView secondary;
		}
	}
}