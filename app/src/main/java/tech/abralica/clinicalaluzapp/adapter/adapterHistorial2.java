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
import tech.abralica.clinicalaluzapp.models.Historial;

public class adapterHistorial2  extends RecyclerView.Adapter<adapterHistorial2.ViewHolderDatos>  implements View.OnClickListener {


    ArrayList<Historial> listaHistorial;

    public adapterHistorial2(ArrayList<Historial> listaHistorial) {
        this.listaHistorial = listaHistorial;
    }
    @Override
    public void onClick(View v) {

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate( R.layout.item_historial,parent,false);
        vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            //  final Dato dato=
            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.fecha.setText(listaHistorial.get(position).getFecha());
            datgolder.nombremedico.setText(listaHistorial.get(position).getNombreMedico());
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
            nombremedico=(TextView)itemView.findViewById( R.id.tvpaciente);
            tvestado=(TextView)itemView.findViewById(R.id.tvestado1);

        }
    }
}
