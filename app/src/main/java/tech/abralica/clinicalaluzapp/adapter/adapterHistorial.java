package tech.abralica.clinicalaluzapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.ClsCita;
import tech.abralica.clinicalaluzapp.models.Historial;

public class adapterHistorial  extends RecyclerView.Adapter<adapterHistorial.ViewHolderDatos>  implements View.OnClickListener{

    ArrayList<Historial> listaHistorial;

    public adapterHistorial(ArrayList<Historial> listaHistorial) {
        this.listaHistorial = listaHistorial;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }
    private View.OnClickListener listener;

    public  void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }


    @NonNull
    @NotNull
    @Override
    public adapterHistorial.ViewHolderDatos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate( R.layout.item_historial,parent,false);
        vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);

    }
    public  void filtrarPaciente(ArrayList<Historial> filtro){
        this.listaHistorial=filtro;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull adapterHistorial.ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            //  final Dato dato=
            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.fecha.setText(listaHistorial.get(position).getFecha());
            datgolder.noombrepaciente.setText(listaHistorial.get(position).getNombrepaciente());
            datgolder.tvestado.setText(listaHistorial.get(position).getEstado());

        }
    }

    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView fecha,hora,nombremedico,noombrepaciente,especialidad,tvestado;
        public ViewHolderDatos(@NonNull @NotNull View itemView) {
            super(itemView);
            fecha=(TextView)itemView.findViewById( R.id.tvfecha);
            noombrepaciente=(TextView)itemView.findViewById( R.id.tvpaciente);
            tvestado=(TextView)itemView.findViewById(R.id.tvestado1);
        }
    }
}
