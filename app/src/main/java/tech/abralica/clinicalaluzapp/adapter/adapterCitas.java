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
import tech.abralica.clinicalaluzapp.models.Citas;
import tech.abralica.clinicalaluzapp.models.ClsCita;

public class adapterCitas extends RecyclerView.Adapter<adapterCitas.ViewHolderDatos>  implements View.OnClickListener {

    ArrayList<ClsCita> listaPersonaje;

    public adapterCitas(ArrayList<ClsCita> listaPersonaje) {
        this.listaPersonaje = listaPersonaje;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate( R.layout.item_citas_reservadas,parent,false);
        vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            //  final Dato dato=
            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.fecha.setText(listaPersonaje.get(position).getFecha());
            datgolder.hora.setText(listaPersonaje.get(position).getHora());
            datgolder.nombremedico.setText(listaPersonaje.get(position).getNombreMedico());
            datgolder.tvestado.setText(listaPersonaje.get(position).getEstado());

        }
    }

    @Override
    public int getItemCount() {
        return listaPersonaje.size();
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
    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView fecha,hora,nombremedico,especialidad,tvestado;
        public ViewHolderDatos(@NonNull @NotNull View itemView) {
            super(itemView);
            fecha=(TextView)itemView.findViewById( R.id.idtvFechaCita);
            hora=(TextView)itemView.findViewById( R.id.idtvHoraCita);
            nombremedico=(TextView)itemView.findViewById( R.id.idtvNombreMedico);
            tvestado=(TextView)itemView.findViewById(R.id.tvestado);

        }
    }
}
