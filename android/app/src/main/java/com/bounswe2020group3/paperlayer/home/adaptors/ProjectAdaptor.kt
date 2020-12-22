package com.bounswe2020group3.paperlayer.home.adaptors

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bounswe2020group3.paperlayer.R
import com.bounswe2020group3.paperlayer.home.cards.ProjectUpdateCard
import com.bounswe2020group3.paperlayer.invite.UserInviteAdapter


private const val TAG = "ProjectAdapter"

interface OnCardClickListener{
    fun onInviteButtonClick(item: ProjectUpdateCard, position: Int )
}
class ProjectAdaptor (var clickListener: OnCardClickListener) : RecyclerView.Adapter<ProjectAdaptor.ProjectViewHolder>() {
    private var projects: List<ProjectUpdateCard> = ArrayList()


    override fun getItemCount(): Int {
        return projects.size
    }


    fun submitList(projectUpdateCardlist : List<ProjectUpdateCard>){
        projects=projectUpdateCardlist
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item_invite, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        when(holder){
            is ProjectViewHolder ->{
                holder.bind(projects[position],clickListener)
            }
        }
    }

    class ProjectViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {

        val projectTitle: TextView =itemView.findViewById<TextView>(R.id.textViewProjectTitle)
        val projectBody: TextView =itemView.findViewById<TextView>(R.id.textViewProjectDescription)
        val projectCreator: TextView =itemView.findViewById<TextView>(R.id.textViewProjectCreator)
        val projectType: TextView =itemView.findViewById<TextView>(R.id.TextViewProjectType)
        val buttonCollab : Button = itemView.findViewById<Button>(R.id.inviteButton)

        fun bind(item : ProjectUpdateCard, action: OnCardClickListener){
            projectTitle.text = item.projectTitle
            projectBody.text = item.projectBody
            projectCreator.text = item.projectCreator
            projectType.text = item.projectType
            Log.i(TAG,"${item.projectTitle} binded") //information
            //Listeners for each project cards
            buttonCollab.setOnClickListener {
                action.onInviteButtonClick(item,adapterPosition)
            }



        }

    }
}