package com.example.quanlymonhoc;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editTextTenMon, editTextSoTinChi;
    Button buttonThem;
    ListView listViewMonHoc;
    ArrayList<MonHoc> monHocList;
    ArrayAdapter<MonHoc> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        editTextTenMon = findViewById(R.id.editTextTenMon);
        editTextSoTinChi = findViewById(R.id.editTextSoTinChi);
        buttonThem = findViewById(R.id.buttonThem);
        listViewMonHoc = findViewById(R.id.listViewMonHoc);

        // Khởi tạo danh sách và adapter
        monHocList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, monHocList);
        listViewMonHoc.setAdapter(adapter);

        // Sự kiện thêm môn học
        buttonThem.setOnClickListener(v -> {
            // Lấy dữ liệu từ EditText
            String tenMon = editTextTenMon.getText().toString().trim();
            String soTinChiStr = editTextSoTinChi.getText().toString().trim();

            // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
            if (tenMon.isEmpty() || soTinChiStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đủ tên môn và số tín chỉ!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int soTinChi = Integer.parseInt(soTinChiStr);
                // Thêm môn học vào danh sách
                monHocList.add(new MonHoc(tenMon, soTinChi));
                // Cập nhật ListView
                adapter.notifyDataSetChanged();
                // Xóa trống các ô nhập liệu sau khi thêm
                editTextTenMon.setText("");
                editTextSoTinChi.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Số tín chỉ không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        // *** PHẦN CODE MỚI: XỬ LÝ CẬP NHẬT VÀ XÓA ***
        listViewMonHoc.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy môn học được chọn tại vị trí 'position'
            MonHoc selectedMonHoc = monHocList.get(position);

            // Tạo một Dialog để hiển thị các tùy chọn
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Chọn hành động cho môn: " + selectedMonHoc.getTenMon());
            builder.setItems(new CharSequence[]{"Cập nhật số tín chỉ", "Xóa môn học", "Hủy"}, (dialog, which) -> {
                switch (which) {
                    case 0: // Cập nhật số tín chỉ
                        showUpdateDialog(selectedMonHoc);
                        break;
                    case 1: // Xóa môn học
                        showDeleteConfirmationDialog(selectedMonHoc);
                        break;
                    case 2: // Hủy
                        dialog.dismiss();
                        break;
                }
            });
            builder.create().show();
        });
    }

    /**
     * Hiển thị dialog để cập nhật số tín chỉ
     * @param monHoc Môn học cần cập nhật
     */
    private void showUpdateDialog(MonHoc monHoc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật số tín chỉ");

        // Tạo một EditText để người dùng nhập số tín chỉ mới
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Nhập số tín chỉ mới");
        input.setText(String.valueOf(monHoc.getSoTinChi())); // Hiển thị số tín chỉ cũ
        builder.setView(input);

        // Thiết lập nút "Cập nhật"
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String newSoTinChiStr = input.getText().toString().trim();
            if (!newSoTinChiStr.isEmpty()) {
                try {
                    int newSoTinChi = Integer.parseInt(newSoTinChiStr);
                    monHoc.setSoTinChi(newSoTinChi); // Cập nhật số tín chỉ trong đối tượng
                    adapter.notifyDataSetChanged(); // Báo cho adapter cập nhật lại giao diện
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Số tín chỉ không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Số tín chỉ không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });
        // Thiết lập nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Hiển thị dialog xác nhận xóa
     * @param monHoc Môn học cần xóa
     */
    private void showDeleteConfirmationDialog(MonHoc monHoc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa môn '" + monHoc.getTenMon() + "' không?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            monHocList.remove(monHoc); // Xóa môn học khỏi danh sách
            adapter.notifyDataSetChanged(); // Cập nhật lại ListView
            Toast.makeText(this, "Đã xóa môn học!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
