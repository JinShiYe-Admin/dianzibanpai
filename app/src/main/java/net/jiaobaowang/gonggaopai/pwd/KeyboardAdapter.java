package net.jiaobaowang.gonggaopai.pwd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.jiaobaowang.gonggaopai.R;

import java.util.List;

/**
 * Created by wang on 2017/6/23.
 */

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.KeyboardHolder> {

    private Context context;
    private List<String> datas;
    private OnKeyboardClickListener listener;

    public KeyboardAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public KeyboardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_key_board, parent, false);
        KeyboardHolder holder = new KeyboardHolder(view);
        setListener(holder);
        return holder;
    }

    private void setListener(final KeyboardHolder holder) {
        holder.tvKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onKeyClick(view, holder, holder.getAdapterPosition());
                }
            }
        });
        holder.rlDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteClick(view, holder, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(KeyboardHolder holder, int position) {
        if (position == 10) {
            holder.rlDel.setVisibility(View.VISIBLE);
            holder.tvKey.setVisibility(View.GONE);
        } else {
            holder.tvKey.setText(datas.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class KeyboardHolder extends RecyclerView.ViewHolder {

        public TextView tvKey;
        public RelativeLayout rlDel;
        private View convertView;

        public KeyboardHolder(View itemView) {
            super(itemView);
            convertView = itemView;
            tvKey = (TextView) itemView.findViewById(R.id.tv_key);
            rlDel = (RelativeLayout) itemView.findViewById(R.id.rl_del);
        }

        public View getconvertView() {
            return convertView;
        }
    }

    public interface OnKeyboardClickListener {

        void onKeyClick(View view, RecyclerView.ViewHolder holder, int position);

        void onDeleteClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnKeyboardClickListener(OnKeyboardClickListener listener) {
        this.listener = listener;
    }
}
