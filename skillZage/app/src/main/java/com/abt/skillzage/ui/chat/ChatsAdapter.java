package com.abt.skillzage.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.chat.model.ChatMsg;

import java.util.List;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
	
	private Context context;
	public List<ChatMsg> chats;

	
	public ChatsAdapter(Context context, List<ChatMsg> chats ) {
		this.context = context;
		this.chats = chats;
	}

	@Override
	public int getItemViewType(int position) {
		// Just as an example, return 0 or 2 depending on position
		// Note that unlike in ListView adapters, types don't have to be contiguous
		int t = 1;
		if(chats.get(position).getMSGTYPE().equalsIgnoreCase("0")){  // 0 means msg send from the current user and 1 means message has recieved by the current user
			t = 1; //OWN MSG
		}else{
			t = 2; // RECIEVED MSG
		}
		return t;
	}
	
	@NonNull
	@Override
	public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v  = null;
		//Log.d("inside msg",  i+"   Testttttttttt      "+chats.get(i).getMSGTYPE());
		if(i == 2){
			v = LayoutInflater.from(context).inflate(R.layout.chat_incoming_row, viewGroup, false);
		}else{
			v = LayoutInflater.from(context).inflate(R.layout.chat_outgoing_row, viewGroup, false);
		}

		return new ChatsViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ChatsViewHolder chatsViewHolder, int i) {

		ChatMsg chat = chats.get(i);
		chatsViewHolder.tvName.setText(chat.getUserName());
		chatsViewHolder.tvMsg.setText(chat.getMessage());
		chatsViewHolder.tvTime.setText(chat.getDatetime());

		
	}


	
	@Override
	public int getItemCount() {
		return chats.size();
	}
	
	class ChatsViewHolder extends RecyclerView.ViewHolder {
		AppCompatImageView profilePic;
		AppCompatTextView tvName;
		AppCompatTextView tvMsg;
		AppCompatTextView tvTime;

		
		ChatsViewHolder(@NonNull View itemView) {
			super(itemView);
		
			profilePic = itemView.findViewById(R.id.profile_image);
			tvName = itemView.findViewById(R.id.tvName);
			tvMsg = itemView.findViewById(R.id.tvMsg);
			tvTime = itemView.findViewById(R.id.tvTime);

		}
	}
}
