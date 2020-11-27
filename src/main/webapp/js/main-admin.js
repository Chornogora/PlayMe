$(document).ready(function() {
    showToasts();
    attachFiltersEventHandler();
    attachUserControlButtonsHandlers();
    attachSortingImagesHandlers();
    attachPaginationButtonsHandlers()
});

function showToasts(){
    const status = $('#show-toast-condition').data('operation-status');
    if(status == constants.STATUS.SUCCESSFUL){
        $('.successful-toast').toast('show');
    }
}

function attachFiltersEventHandler(){
    $("#apply-filters-button").on('click', ()=>{
        $("#offset-input").val(0);
        applyFilterParameters();
    });

    $("#clear-filters-button").on('click', ()=>{
        location.href = "/PlayMe/admin/users";
    });
}

function applyFilterParameters(){
    const parameters = {
        "login-filter": $("#login-filter").val(),
        "email-filter": $("#email-filter").val(),
        "first-name-filter": $("#first-name-filter").val(),
        "last-name-filter": $("#last-name-filter").val(),
        "birthdate-from-filter": $("#birthdate-filter-from").val(),
        "birthdate-to-filter": $("#birthdate-filter-to").val(),
        "creation-date-from-filter": $("#creation-date-filter-from").val(),
        "creation-date-to-filter": $("#creation-date-filter-to").val(),
        "role-filter": $("input[name='role-filter']:checked").map(function() {
            return $(this).val();
        }).get(),
        "status-filter": $("input[name='status-filter']:checked").map(function() {
            return $(this).val();
        }).get(),
        "sorting-field": $("[data-status]").data('field') || '',
        "sorting-type": $("[data-status]").attr('data-status') || '',
        "limit": $("#limit-input").val(),
        "offset": $("#offset-input").val()
    }
    location.href = "/PlayMe/admin/users?"+ new URLSearchParams(parameters);
}

function attachUserControlButtonsHandlers(){
    $('.edit-user-button').on('click', (event)=>{
        const userId = $(event.target).data(constants.USER_ID_ATTRIBUTE_NAME);
        location.href = `/PlayMe/admin/user/edit/${userId}`;
    });

    $('#delete-user-modal').on('show.bs.modal', function (event) {
      const button = $(event.relatedTarget);
      const userId = $(button).data('user-id');
      const login = $(button).data('user-login');
      $("#modal-message-text").text(`Are you sure want to delete user ${login}?`);
      $("#modal-confirm-button").on('click', ()=>{
        const formData = new FormData();
        formData.append('userId', userId);
        fetch("/PlayMe/admin/user/delete/" + userId, {
            method: 'DELETE',
        }).then(()=>location.reload());
      });
    });
}

function attachSortingImagesHandlers(){
    $(".arrow-image").on('click', (event)=>{
        const image = event.target;
        const status = $(image).data("status");
        $("[data-status]").attr('data-status', null);
        $("[data-status]").data('status', null);
        switch(status){
            case 'ASC':
                $(image).attr('data-status', 'DESC');
                break;
            case 'DESC':
                $(image).attr('data-status', null);
                break;
            default:
                $(image).attr('data-status', 'ASC');
        }
        applyFilterParameters();
    });
}

function attachPaginationButtonsHandlers(){
    const limit = $("#limit-input").val();
    const offset = $("#offset-input").val();

    $("#pagination-left-button").on('click', (event)=>{
        $("#offset-input").val(offset-limit);
        applyFilterParameters();
    });

    $("#pagination-right-button").on('click', (event)=>{
        $("#offset-input").val(offset+limit);
        applyFilterParameters();
    });
}