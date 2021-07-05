package tech.abralica.clinicalaluzapp.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Historial;
import tech.abralica.clinicalaluzapp.models.Medico;

public class adapterMedicos  extends RecyclerView.Adapter<adapterMedicos.ViewHolderDatos> implements View.OnClickListener{


    ArrayList<Medico> listaMeidico;

    public adapterMedicos(ArrayList<Medico> listaMeidico) {
        this.listaMeidico = listaMeidico;
    }

    private View.OnClickListener listener;
    public  void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicos,parent,false);
        vista.setOnClickListener(this);
        return new  ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull adapterMedicos.ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            final ViewHolderDatos items =(ViewHolderDatos)holder;
            items.tvnombre.setText(listaMeidico.get(position).getNombres());
            items.tvdetalle.setText(listaMeidico.get(position).getApellidos());
            if (listaMeidico.get(position).getFotoPerfil().equals("default_image")){
                items.imgmedico.setImageResource(R.drawable.imgdoctorico);
            }else{
                Picasso.get().load(listaMeidico.get(position).getFotoPerfil()).fit().centerCrop().into(items.imgmedico);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaMeidico.size();
    }


    public  void filtrarMedicos(ArrayList<Medico> filtro){
        this.listaMeidico=filtro;
        notifyDataSetChanged();
    }



    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView tvnombre,tvdetalle,tvsedesalon;
        int id_salon;
        ImageView imgmedico;
        String rutafoto;
        public ViewHolderDatos(@NonNull @NotNull View itemView) {
            super(itemView);

            tvnombre=(TextView)itemView.findViewById(R.id.tvnombredoctor);
            imgmedico=(ImageView)itemView.findViewById(R.id.imgmedico);
            tvdetalle=(TextView)itemView.findViewById(R.id.iddetalle);

        }
    }
}
