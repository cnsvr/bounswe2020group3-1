package com.bounswe2020group3.paperlayer.project

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.Navigation
import com.bounswe2020group3.paperlayer.R
import com.bounswe2020group3.paperlayer.home.data.CollaborateRequest
import com.bounswe2020group3.paperlayer.mvp.BasePresenter
import com.bounswe2020group3.paperlayer.project.data.Project
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Inject

private const val TAG = "Project Detail Presenter"

class ProjectPresenter  @Inject constructor(private var model: ProjectDetailContract.Model) : BasePresenter<ProjectDetailContract.View>(),ProjectDetailContract.Presenter {

    //Disposable
    private var disposable = CompositeDisposable()
    private var project: Project? = null

    override fun setView(view: ProjectDetailContract.View) {
        this.view =view
    }

    override fun showMessage(message: String) {
        this.view?.showToast(message)
    }

    override fun bind(view: ProjectDetailContract.View) {
        super.bind(view)
        this.view?.writeLogMessage("i",TAG,"Project Detail Presenter Created")
        this.view?.resetProjectUI()

        subscribeAuthToken()
    }

    override fun subscribeAuthToken() {
        this.view?.writeLogMessage("i",TAG, "##1##")
        val userProfileSub = model.getAuthToken().subscribe ({ token ->
            this.view?.updateCurrentUser(token.id)
        },{err ->
            this.view?.writeLogMessage("e",TAG,"error while auth tokening, $err")
        })
        disposable.add(userProfileSub)
    }

    override fun unbind() {
        super.unbind()
        disposable.clear()
    }

    override fun setProject(project: Project){
        this.project = project
    }

    override fun getProject(): Project? {
        if (project == null) return null
        return project
    }

    override fun fetchFiles(projectId: Int)  {
        this.view?.writeLogMessage("i",TAG,"Fetching the files...")
        val getFilesObservable = model.fetchFiles(projectId).subscribe(
            { fileList ->
                this.view?.writeLogMessage("i",TAG,"files Fetching Successful. $projectId")
                for( file in fileList){
                    this.view?.addFileCard(FileCard(file.id,file.name,false,""))
                }
                this.view?.submitFileCardList()

            },
            { error ->
                this.view?.writeLogMessage("e",TAG,"Error in fetching the files $error")
            }
        )
        disposable.add(getFilesObservable)
    }



    //Fetch project and update ui
    override fun fetchProject(projectId: Int) {
        this.view?.writeLogMessage("i",TAG,"Fetching the project...")
        val getProjectObservable = model.getProject(projectId).subscribe(
                { project ->
                    this.view?.writeLogMessage("i",TAG,"Fetching Successful.")
                    this.view?.updateProjectUI(project)
                    setProject(project)
                },
                { error ->
                    this.view?.writeLogMessage("e",TAG,"Error in fetching project")
                }
        )
        disposable.add(getProjectObservable)
    }

    override fun uploadFile(file: File) {
        TODO("Not yet implemented")
    }

    override fun fetchRequestOfMine(projectId: Int) {
        this.view?.writeLogMessage("i",TAG,"Fetching the request...")

        val getRequestObservable = model.fetchRequestofMine(projectId).subscribe(
                { list ->
                    this.view?.writeLogMessage("i",TAG,"Fetching Successful.")
                    if(list.size != 0)
                        view?.collabCheck(list[0].id)
                    else
                        view?.collabUncheck()
                },
                { error ->
                    this.view?.writeLogMessage("e",TAG,"Error in fetching requests")
                }
        )
        if(getRequestObservable != null)
            disposable.add(getRequestObservable)
    }

    override fun navigateToEditProject() {
        val bundle = Bundle()
        bundle.putParcelable("PROJECT", getProject())
        view?.getLayout()?.let { Navigation.findNavController(it).navigate(R.id.navigateToProjectEditFromProjectFragment, bundle) }
    }

    override fun OnClickCollab(projectId: Int,collabbed : Int) {
        if(collabbed == -1) {
            val collaborateObservable = model.collaborationRequest(CollaborateRequest(projectId, "hello")).subscribe({ request->

                view?.writeLogMessage("i", TAG, "request sent")
                view?.showToast("Request sent.")
                view?.collabCheck(request.id)
                view?.writeLogMessage("i",TAG,"request to project ${request.to_project} with the requestid ${request.id}")
                disposable.clear()

            }, {
                view?.writeLogMessage("i",TAG, "Error while sending a request to project  with the id ${projectId} " + it)

                view?.showToast("Error while sending a request to project with the id ${projectId} " + it)
                disposable.clear()

            })
            disposable.add(collaborateObservable)
        }
        else{

            view?.writeLogMessage("i", TAG,"request withdrawal for $collabbed sent")

            val uninviteUserObservable = model.deleteRequest(collabbed).subscribe({
                view?.writeLogMessage("i",TAG,"request withdrawal successful")
                view?.collabUncheck()
                disposable.clear()
            },
                    {
                        view?.writeLogMessage("e", TAG,"request withdrawal unsuccessful $it")
                        disposable.clear()
                    })
            disposable.add(uninviteUserObservable)
        }
    }
}