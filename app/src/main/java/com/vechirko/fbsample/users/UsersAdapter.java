package com.vechirko.fbsample.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.user.UserActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

    List<UserModel> data = new ArrayList<>();

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        UserModel user = data.get(position);
        holder.username.setText("@".concat(user.getUsername()));
        holder.name.setText(user.getName());
        holder.address.setText(user.getAddress().toString());
        holder.phone.setText(user.getPhone());
        holder.website.setText(user.getWebsite());

        holder.itemView.setOnClickListener(v -> openPost(v.getContext(), user));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void openPost(Context c, UserModel user) {
        c.startActivity(UserActivity.newIntent(c, user.getId()));
    }

    public void setData(Collection<UserModel> list) {
        data = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    static class UserHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView username;
        TextView name;
        TextView address;
        TextView phone;
        TextView website;

        public UserHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.username);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            website = itemView.findViewById(R.id.website);
        }
    }
}
