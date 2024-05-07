package com.compscicomputations.utils

/*
class LoadingDialog(private val activity: Activity?) {

    private var dialog: Dialog? = null
    private var loadingView :LinearLayout? = null
    private var loadingAnimationView :LottieAnimationView? = null
    private var doneView :LinearLayout? = null
    private var doneAnimationView : LottieAnimationView? = null
    private var errorView :LinearLayout? = null
    private var errorAnimationView : LottieAnimationView? = null
    private var loadingText: TextView? = null
    private var doneText: TextView? = null
    private var errorText: TextView? = null
    private var cancelAction: ImageView? = null

    private var shownView = "loadingView"

    init {
        if (activity != null) {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.custom_loading_dialog)
            dialog.setCancelable(false)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.CENTER)

            this.dialog = dialog
            this.loadingView = dialog.findViewById(R.id.loading_view)
            this.loadingAnimationView = dialog.findViewById(R.id.loading_anim)
            this.doneView = dialog.findViewById(R.id.done_view)
            this.doneView?.visibility = View.GONE
            this.doneAnimationView = dialog.findViewById(R.id.done_anim)
            this.errorView = dialog.findViewById(R.id.error_view)
            this.errorView?.visibility = View.GONE
            this.errorAnimationView = dialog.findViewById(R.id.error_anim)
            this.loadingText = dialog.findViewById(R.id.progress_text)
            this.doneText = dialog.findViewById(R.id.done_text)
            this.errorText = dialog.findViewById(R.id.error_text)
            this.cancelAction = dialog.findViewById(R.id.cancel_action)
        }
    }

    fun setText(text: String) {
        if (activity == null) {
            dismiss()
            return
        }

        when (shownView){
            "loadingView" -> loadingText?.text = text
            "doneView" -> doneText?.text = text
            "errorView" -> errorText?.text = text
        }
    }

    fun show(text: String? = null) {
        if (activity == null) {
            dismiss()
            return
        } else {
            loadingView?.visibility = View.VISIBLE
            doneView?.visibility = View.GONE
            errorView?.visibility = View.GONE
            cancelAction?.visibility = View.VISIBLE

            shownView = "loadingView"
        }

        loadingAnimationView?.playAnimation()
        if (dialog?.isShowing != true) {
            dialog?.show()
        }
        if (!text.isNullOrEmpty()) loadingText?.text = text
    }

    fun isDone(text: String? = null, onSuccess : (dialog : Dialog?) -> Unit) {
        if (activity == null) {
            dialog?.dismiss()
            return
        } else {
            loadingView?.visibility = View.GONE
            doneView?.visibility = View.VISIBLE
            errorView?.visibility = View.GONE
            cancelAction?.visibility = View.GONE

            shownView = "doneView"
        }

        doneAnimationView?.playAnimation()
        if (!text.isNullOrEmpty()) doneText?.text = text
        if (dialog?.isShowing != true) {
            dialog?.show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
            onSuccess(this.dialog)
        },2000)
    }

    fun isError(text: String? = null, onFailure : (dialog : Dialog?) -> Unit) {
        if (activity == null) {
            dialog?.dismiss()
            return
        } else {
            loadingView?.visibility = View.GONE
            doneView?.visibility = View.GONE
            errorView?.visibility = View.VISIBLE
            cancelAction?.visibility = View.GONE

            shownView = "errorView"
        }

        errorAnimationView?.playAnimation()
        if (!text.isNullOrEmpty()) errorText?.text = text
        if (dialog?.isShowing != true) {
            dialog?.show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
            onFailure(this.dialog)
        },3500)
    }

    fun onCancel(onCancel : (dialog : Dialog?) -> Unit) {
        cancelAction?.setOnClickListener {
            onCancel(this.dialog)
        }
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}*/
