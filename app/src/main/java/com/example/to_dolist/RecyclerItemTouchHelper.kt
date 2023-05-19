package com.example.to_dolist

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.Adapter.ToDoAdapter

class RecyclerItemTouchHelper(private val adapter: ToDoAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder(adapter.getContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this Task?")
                .setPositiveButton("Confirm") { dialog, _ ->
                    adapter.deleteItem(position)
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val icon: Drawable?
        val background: ColorDrawable

        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 20

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_edit_24)
            background = ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.purple_500))
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_24)
            background = ColorDrawable(Color.RED)
        }

        val iconMargin = (itemView.height - icon?.intrinsicHeight!!) / 2
        val iconTop = itemView.top + (itemView.height - icon?.intrinsicHeight!!) / 2
        val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)

        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + (icon?.intrinsicWidth ?: 0)
            icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset,
                itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - (icon?.intrinsicWidth ?: 0)
            val iconRight = itemView.right - iconMargin
            icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon?.draw(c)
    }
}
